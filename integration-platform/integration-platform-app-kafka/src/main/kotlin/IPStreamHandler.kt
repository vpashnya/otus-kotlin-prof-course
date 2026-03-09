package ru.pvn.integration.platform

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import java.time.Duration
import kotlin.String
import kotlin.collections.forEach
import apiV1RequestDeserialize
import apiV1ResponseSerialize
import fromTransport
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.learning.IPContext
import ru.pvn.learning.helpers.makeIPError
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPState.FAILING
import ru.pvn.learning.models.IPWorkMode
import toTransport

class IPStreamHandler(
  val applicationSettings: ApplicationSettings,
  val consumer: KafkaConsumer<String, String>,
  val producer: KafkaProducer<String, String>,
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

        if (!records.isEmpty) {
          logger.info("Receive ${records.count()} messages")
        }

        records.forEach { record: ConsumerRecord<String, String> ->
          try {
            val context = IPContext(
              workMode = when (applicationSettings.mode) {
                Mode.PROD -> IPWorkMode.PROD
                Mode.STUB -> IPWorkMode.STUB
                Mode.TEST -> IPWorkMode.TEST
              }
            )

//            context.command = IPCommand.CREATE

            val resRecord = try {
              val iRequest = apiV1RequestDeserialize<IRequest>(record.value())
              logger.info("iRequest : ${iRequest.javaClass}")
              context.fromTransport(iRequest)

              logger.info("context.command : ${context.command}")

              val processor = applicationSettings.ipStreamProcessor
              processor.exec(context)
              logger.info("Message processed")
              processor.exec(context)


              ProducerRecord(
                topics.outgoing,
                "IPStream",
                apiV1ResponseSerialize(context.toTransport())
              )

            } catch (ee: Throwable) {
              logger.info("Message failed")
              context.state = FAILING
              context.errors.add(ee.makeIPError())
              ProducerRecord(
                topics.outgoing,
                "IPStream",
                apiV1ResponseSerialize(context.toTransport())
              )
            }

            logger.info("sending ${record.value()}")
            withContext(Dispatchers.IO) {
              producer.send(resRecord)
            }
          } catch (ex: Exception) {
            logger.error(ex.message)
          }
        }
      }
    } catch (e: Exception) {
      logger.error(e.message)
    }
  }

  override fun close() {
    isWork.value = false
  }
}