package ru.pvn.learning.processor.kafka.processor.metadata

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.Consumer
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.learning.processor.kafka.processor.handlers.IntegrationStreamsStarter
import java.lang.AutoCloseable
import java.time.Duration

interface MetaDataActualizer {
  fun start(): Unit
}

class MetaDataActualizerImpl(
  private val consumer: Consumer<String, String>,
  private val refreshTopic: String,
  private val streamsStarter: IntegrationStreamsStarter,
  private val logger: Logger = LoggerFactory.getLogger(MetaDataActualizerImpl::class.java),
) : MetaDataActualizer, AutoCloseable {
  private val isWork = atomic(true)

  override fun start(): Unit = runBlocking {
    try {
      consumer.subscribe(listOf(refreshTopic))
      while (isWork.value) {
        val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
          consumer.poll(Duration.ofSeconds(1))
        }
        if (!records.isEmpty) {
          records.forEach { record: ConsumerRecord<String, String> ->
            logger.info("Receive <<${record.value()}>>")
          }
          streamsStarter.restart()
        }
      }
      consumer.close()
    } catch (e: Exception) {
      logger.error("MetaDataActualizer failed : ${e.message}")
    }
  }

  override fun close() {
    isWork.value = false
  }
}