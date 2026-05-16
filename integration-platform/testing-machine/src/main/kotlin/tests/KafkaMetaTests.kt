package ru.pvn.learning.tests

import apiV1RequestSerialize
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.DisplayName
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import kotlin.random.Random
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.pvn.learning.applicationConfig
import ru.pvn.learning.config.ApplicationConfigData
import java.time.Duration


class KafkaMetaTests {

  private val logger = LoggerFactory.getLogger("KafkaMetaTests")
  private val applicationConfigData = (applicationConfig as ApplicationConfigData)

  enum class MonolithClasses {
    PR_CRED, MAIN_DOCUM, DOCUMENT, KRED_CORP, DEPOSIT_PRIV, DEPOSIT_ORG, BASE_VAL_OP
  }

  enum class MonolithMethods {
    NEW_AUTO, EDIT_AUTO, DELETE_AUTO, LIB, CALC_PARAMS,
  }

  enum class TransportParams {
    ex1, ex2, ex3
  }


  @DisplayName("Kafka - tests")
  @Test
  fun test() {
    val streams = buildList {
      MonolithClasses.entries.forEach { cl ->
        MonolithMethods.entries.forEach { mth ->
          if (Random.nextBoolean())
            add(IntegrationStream(cl, mth, TransportParams.entries.random()))
        }
      }
    }

    logger.info("---=== create metadata ===---")
    sendMetadataToKafka(streams)

    logger.info("---=== receive metadata ===---")
    receiveMetadataFromKafka()

  }

  private fun sendMetadataToKafka(streams: List<IntegrationStream>) {

    applicationConfigData.createKafkaProducer().use { producer ->
      val records = streams.map { stream ->
        val createRequest = StreamCreateRequest(
          requestType = "CREATE",
          stream = StreamCreateObject(
            classShortName = stream.mClass.name,
            methodShortName = stream.mMethod.name,
            transportParams = stream.mTransportParams.name,
            description = "Отправка информации в ФНС"
          )
        )
        ProducerRecord<String, String>(
          applicationConfigData.kafkaIPStreamTopicIn,
          null,
          apiV1RequestSerialize(createRequest)
        )
      }

      records.forEach { record ->
        producer.send(record) { metadata, exception ->
          if (exception == null)
            logger.info("Sent: $record with offset ${metadata.offset()}")
          else
            logger.info(exception.toString())

        }
      }
      producer.flush()
    }
  }

  private fun receiveMetadataFromKafka() {
    val consumer = applicationConfigData.createKafkaConsumer()
    consumer.subscribe(listOf(applicationConfigData.kafkaIPStreamTopicOut))
    try {
      val records = consumer.poll(Duration.ofMillis(1000)) // Poll for records
      for (record in records) {
        logger.info("Received record: offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()} in partition ${record.partition()}")
      }
    } catch (e: Exception) {
      logger.info(e.message)
    } finally {
      consumer.close()
    }
  }

  data class IntegrationStream(
    val mClass: MonolithClasses,
    val mMethod: MonolithMethods,
    val mTransportParams: TransportParams,
  )

}