package ru.pvn.learning.processor.kafka.processor.handlers

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.core.Closeable
import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.learning.processor.config.ApplicationConfig
import ru.pvn.learning.processor.kafka.processor.metadata.IPStreamRecord
import ru.pvn.learning.processor.kafka.processor.metadata.MetaDataDownloader
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import kotlin.String

interface IntegrationStreamsStarter {
  fun restart(): Unit
}

class IntegrationStreamsStarterImpl(
  private val config: ApplicationConfig,
  private val metadataDownloader: MetaDataDownloader,
  private val logger: Logger = LoggerFactory.getLogger(IntegrationStreamsStarterImpl::class.java),
  private val statLogger: Logger = LoggerFactory.getLogger("stat.logger"),
  private val httpClient: HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
  },
) : IntegrationStreamsStarter, Closeable {
  private var isWork = atomic(true)
  private val allStreams = ConcurrentHashMap<IPStreamRecord, Boolean>()
  private val workingCoroutines = mutableSetOf<IPStreamRecord>()

  override fun restart(): Unit = runBlocking {
    allStreams.putAll(metadataDownloader.download())
    allStreams.forEach { (stream, _) -> runStream(isWork, stream) }

  }

  private fun runStream(isWork: AtomicBoolean, ipStream: IPStreamRecord) = runBlocking {
    if (ipStream !in workingCoroutines) {
      workingCoroutines.add(ipStream)
      CoroutineScope(Dispatchers.Default).launch {
        val topicIn = ipStream.topicIn()
        val topicOut = ipStream.topicOut()

        val consumer = config.createKafkaConsumer(ipStream.groupIdSuffix())
        val producer = config.createKafkaProducer()

        logger.info("creating $consumer")
        logger.info("creating $producer")

        try {
          consumer.subscribe(listOf(topicIn))
          while (isWork.value) {
            val records: ConsumerRecords<String, String> = withContext(Dispatchers.Default) {
              consumer.poll(Duration.ofSeconds(1))
            }

            records.forEach { record: ConsumerRecord<String, String> ->
              if (allStreams[ipStream] == true) {
                val monolithResponse: HttpResponse = httpClient.post(config.ancientMonolithUrl) {
                  method = HttpMethod.Post
                  contentType(ContentType.Application.Json)
                  setBody(record.value())
                }
                val responseRecord: ProducerRecord<String, String> =
                  ProducerRecord(topicOut, null, monolithResponse.body())
                producer.send(responseRecord)
                statLogger.info("successful $ipStream")
              } else {
                val responseRecord: ProducerRecord<String, String> = ProducerRecord(topicOut, null, "fail sent to ancient monolith")
                producer.send(responseRecord)
                statLogger.info("fail $ipStream")
              }

            }
            delay(1)
          }
        } catch (e: Exception) {
          logger.error("IntegrationStreamsStarter failed : ${e.message}")
        }
        consumer.close()
        producer.close()
      }
      logger.info("Runed thread for $ipStream")
    }
  }

  override fun close() {
    isWork.value = false
    httpClient.close()
  }

}