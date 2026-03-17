package ru.pvn.integration.platform.kafka

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.kafka.v1.processV1
import java.time.Duration
import kotlin.String
import kotlin.collections.forEach


class IPStreamHandler(
  val applicationSettings: ApplicationSettings,
  val consumer: Consumer<String, String>,
  val producer: Producer<String, String>,
  val topics: TopicPair,
) : AutoCloseable {
  private val logger = LoggerFactory.getLogger(IPStreamHandler::class.java)

  private val isWork = atomic(true)
  fun start(): Unit = runBlocking {

    try {
      consumer.subscribe(listOf(topics.incoming))
      while (isWork.value) {
        val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
          consumer.poll(Duration.ofSeconds(1))
        }

        if (!records.isEmpty) { logger.debug("Receive ${records.count()} messages") }

        records.forEach { record: ConsumerRecord<String, String> ->
          val response = processV1(
            applicationSettings = applicationSettings,
            request = record.value()
          )

          val responseRecord = ProducerRecord(topics.outgoing, "IPStream", response)

          withContext(Dispatchers.IO) {
            producer.send(responseRecord)
          }
        }
      }
    } catch (e: Exception) {
      logger.error("Failed ${e.message}")
    }
  }

  override fun close() {
    isWork.value = false
  }
}