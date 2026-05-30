package ru.pvn.learning.tests

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.api.v1.models.*
import ru.pvn.learning.config.ApplicationConfigData
import ru.pvn.learning.config.receiveFromTopic
import ru.pvn.learning.testing.machine.externalsystem.KafkaTransportParams
import ru.pvn.learning.testing.machine.externalsystem.MonolithClasses
import ru.pvn.learning.testing.machine.externalsystem.MonolithMethods
import kotlin.random.Random

class SystemWithKafkaIntegration(
  private val applicationConfigData: ApplicationConfigData,
  private val logger: Logger = LoggerFactory.getLogger("SystemWithKafkaIntegration"),
  private val streamsProducer: Producer<String, String> = applicationConfigData.createKafkaProducer(),
  private val streamsConsumer: Consumer<String, String> = applicationConfigData.createKafkaConsumer("ip-stream")
    .also { it.subscribe(listOf(applicationConfigData.kafkaIPStreamTopicOut)) },
) : AutoCloseable {

  fun sendFillingMetadataToKafka(): String {
    val streams = buildList {
      MonolithClasses.entries.forEach { cl ->
        MonolithMethods.entries.forEach { mth ->
          if (Random.nextInt(10) < 3) {
            add(IntegrationStream(cl, mth, KafkaTransportParams.entries.random()))
          }
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


    val respondText = StringBuilder()
    respondText.append("created random streams:\n")

    receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger)
      .forEach { resp ->
        respondText.append("$resp \n")
      }

    return respondText.toString()

  }

  fun enableRandomStreams(): String {
    val streamsMetadata = getFullMetadata(streamsProducer, streamsConsumer)
    val sendRecords = buildList {
      streamsMetadata
        .streams
        ?.filter { it.transportParams?.contains("kafka") == true }
        ?.forEach { stream ->
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

    val respondText = StringBuilder()
    respondText.append("enabled random streams:\n")

    receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger)
      .forEach { resp ->
        respondText.append("$resp \n")
      }

    return respondText.toString()

  }

  fun disableAllStreams(): String {
    val streamsMetadata = getFullMetadata(streamsProducer, streamsConsumer)
    val sendRecords = buildList {
      streamsMetadata
        .streams
        ?.filter { it.transportParams?.contains("kafka") == true && it.active == true }
        ?.forEach { stream ->
          add(
            ProducerRecord<String, String>(
              applicationConfigData.kafkaIPStreamTopicIn,
              null,
              apiV1RequestSerialize(StreamDisableRequest(streamId = stream.id, version = stream.version))
            )
          )
          logger.info("disabled $stream")
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

    val respondText = StringBuilder()
    respondText.append("Disabled streams:\n")

    receiveFromTopic(streamsConsumer, applicationConfigData.kafkaIPStreamTopicOut, logger)
      .forEach { resp ->
        respondText.append("$resp \n")
      }

    return respondText.toString()

  }

  fun sendSyntheticDataForStreams(): String {
    val streamsMetadata = getFullMetadata(streamsProducer, streamsConsumer)

    val respondText = StringBuilder()
    respondText.append("send to  random streams:\n")

    streamsMetadata.streams
      ?.filter { it.active == true }
      ?.forEach { streamsMetadata ->
        val streamTopic =
          """${streamsMetadata.transportParams}.${streamsMetadata.classShortName}.${streamsMetadata.methodShortName}""".lowercase()
        val streamTopicIn = "${streamTopic}.in"
        val streamTopicOut = "${streamTopic}.out"
        val producer = applicationConfigData.createKafkaProducer()
        val sendRecord = ProducerRecord<String, String>(
          streamTopicIn,
          null,
          "Some data for ancient monolith ${Random.nextLong(1000000)}"
        )
        producer.send(sendRecord)
        producer.flush()
        producer.close()
      }

    streamsMetadata.streams
      ?.filter { it.active == true }
      ?.forEach { streamsMetadata ->
        val streamTopic =
          """${streamsMetadata.transportParams}.${streamsMetadata.classShortName}.${streamsMetadata.methodShortName}""".lowercase()
        val streamTopicOut = "${streamTopic}.out"
        val consumer = applicationConfigData.createKafkaConsumer(streamTopicOut.replace(".", "-"))

        consumer.subscribe(listOf(streamTopicOut))
        val receiverRecords = receiveFromTopic(consumer, streamTopicOut, logger)
        receiverRecords.forEach {
          respondText.append("receive from $streamTopicOut : $it \n")
        }
        consumer.close()
      }


    return respondText.toString()
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

  override fun close() {
    streamsProducer.close()
    streamsConsumer.close()
  }

  data class IntegrationStream(
    val mClass: MonolithClasses,
    val mMethod: MonolithMethods,
    val mTransportParams: KafkaTransportParams,
  )

}