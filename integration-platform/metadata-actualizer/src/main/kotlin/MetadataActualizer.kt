package ru.pvn.learning

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord

interface MetadataActualizer {
  fun sendRefresh()

  companion object {
    val NONE = object : MetadataActualizer {
      override fun sendRefresh() { }
    }
  }
}

class MetadataActualizerImpl(
  private val producer: Producer<String, String>,
  private val topic: String,
  private val initiator: String,
  private val logger: Logger = LoggerFactory.getLogger(MetadataActualizer::javaClass.name),
) : AutoCloseable {

  fun sendRefresh() {
    val record: ProducerRecord<String, String> = ProducerRecord(topic, null, "service initiator $initiator")
    producer.send(record) { metadata, exception ->
      if (exception == null)
        logger.info("Sent: ${metadata.offset()}")
      else
        logger.error("Send error ${exception.message}")
    }
    producer.flush()
  }

  override fun close() {
    producer.close()
  }

}