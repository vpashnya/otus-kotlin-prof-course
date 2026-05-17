package ru.pvn.learning.tests

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.api.v1.models.*
import ru.pvn.learning.applicationConfig
import ru.pvn.learning.config.ApplicationConfigData
import ru.pvn.learning.config.receiveFromTopic
import kotlin.random.Random


class KafkaMetaTests {

  private val logger = LoggerFactory.getLogger("KafkaMetaTests")
  private val applicationConfigData = (applicationConfig as ApplicationConfigData)

  enum class MonolithClasses {
    PR_CRED, MAIN_DOCUM, DOCUMENT, KRED_CORP, DEPOSIT_PRIV, DEPOSIT_ORG, BASE_VAL_OP, FOLDER_PAY, COM_STATUS_PRD
  }

  enum class MonolithMethods {
    NEW_AUTO, EDIT_AUTO, DELETE_AUTO, LIB, CALC_PARAMS
  }

  enum class TransportParams {
    ex1, ex2, ex3
  }


  @DisplayName("Kafka - tests")
  @Test
  fun test() {


    val streamsProducer = applicationConfigData.createKafkaProducer()
    val streamsConsumer = applicationConfigData.createKafkaConsumer()
      .also { it.subscribe(listOf(applicationConfigData.kafkaIPStreamTopicOut)) }


    logger.info("---=== create metadata ===---")
    sendFillingMetadataToKafka(streamsProducer, streamsConsumer)

    logger.info("---=== enable random streams ===---")
    enableRandomStreams(streamsProducer, streamsConsumer)


    streamsProducer.close()
    streamsConsumer.close()

  }


  private fun sendFillingMetadataToKafka(
    streamsProducer: Producer<String, String>,
    streamsConsumer: Consumer<String, String>,
  ) {

    val streams = buildList {
      MonolithClasses.entries.forEach { cl ->
        MonolithMethods.entries.forEach { mth ->
          if (Random.nextBoolean())
            add(IntegrationStream(cl, mth, TransportParams.entries.random()))
        }
      }
    }

    val records = streams.map { stream ->
      val createRequest = StreamCreateRequest(
        requestType = "CREATE",
        stream = StreamCreateObject(
          classShortName = stream.mClass.name,
          methodShortName = stream.mMethod.name,
          transportParams = stream.mTransportParams.name,
          description = "Описание потока ${stream.mTransportParams.name}.${stream.mClass.name}.${stream.mMethod.name}"
        )
      )
      ProducerRecord<String, String>(
        applicationConfigData.kafkaIPStreamTopicIn,
        null,
        apiV1RequestSerialize(createRequest)
      )
    }

    records.forEach { record ->
      streamsProducer.send(record) { metadata, exception ->
        if (exception == null)
          logger.info("Sent: $record with offset ${metadata.offset()}")
        else
          logger.info(exception.toString())
      }
    }
    streamsProducer.flush()

    receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger)
      .forEach {
        logger.info("receive : $it")
      }

  }

  private fun getFullMetadata(
    streamsProducer: Producer<String, String>,
    streamsConsumer: Consumer<String, String>,
  ): StreamAccessibleResponse {
    val record = ProducerRecord<String, String>(
      applicationConfigData.kafkaIPStreamTopicIn,
      null,
      apiV1RequestSerialize(StreamAccessibleRequest())
    )

    streamsProducer.send(record) { metadata, exception ->
      if (exception == null)
        logger.info("Sent: $record with offset ${metadata.offset()}")
      else
        logger.info(exception.toString())
    }

    streamsProducer.flush()

    val response = receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger).first()

    return (apiV1ResponseDeserialize(response) as StreamAccessibleResponse)
  }

  private fun enableRandomStreams(
    streamsProducer: Producer<String, String>,
    streamsConsumer: Consumer<String, String>,
  ) {
    val streamsMetadata = getFullMetadata(streamsProducer, streamsConsumer)

    val sendRecords = buildList {
      streamsMetadata.streams?.forEach { stream ->
        if (Random.nextBoolean()) {
          add(
            ProducerRecord<String, String>(
              applicationConfigData.kafkaIPStreamTopicIn,
              null,
              apiV1RequestSerialize(StreamEnableRequest(streamId = stream.id, version = stream.version))
            )
          )
          logger.info("enabled $stream")
        }
      }
    }

    sendRecords.forEach { record ->
      streamsProducer.send(record) { metadata, exception ->
        if (exception == null)
          logger.info("Sent: $record with offset ${metadata.offset()}")
        else
          logger.info(exception.toString())
      }
    }
    streamsProducer.flush()

    receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger)
      .forEach {
        logger.info("receive : $it")
      }

  }


  private fun createSyntheticDataForStreams(
    streamsProducer: Producer<String, String>,
    streamsConsumer: Consumer<String, String>,
  ) {
    val streamsMetadata = getFullMetadata(streamsProducer, streamsConsumer)


  }

  data class IntegrationStream(
    val mClass: MonolithClasses,
    val mMethod: MonolithMethods,
    val mTransportParams: TransportParams,
  )

}