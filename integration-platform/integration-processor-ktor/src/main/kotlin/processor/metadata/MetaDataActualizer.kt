package ru.pvn.ktor.processor.processor.metadata

import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.Consumer
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.ktor.processor.WorkIPStreams
import java.lang.AutoCloseable
import java.time.Duration

interface MetaDataActualizer {
  fun start(): Unit
}

class MetaDataActualizerImpl(
  private val consumer: Consumer<String, String>,
  private val refreshTopic: String,
  private val logger: Logger = LoggerFactory.getLogger(MetaDataActualizerImpl::class.java),
) : MetaDataActualizer, AutoCloseable {
  private val isWork = atomic(true)

  override fun start(): Unit = runBlocking {
    CoroutineScope(Dispatchers.Default).launch {
      try {
        consumer.subscribe(listOf(refreshTopic))
        while (isWork.value) {
          delay(1000)
          val records: ConsumerRecords<String, String> = withContext(Dispatchers.IO) {
            consumer.poll(Duration.ofSeconds(1))
          }
          if (!records.isEmpty) {
            records.forEach { record: ConsumerRecord<String, String> ->
              logger.info("Receive <<${record.value()}>>")
            }
            WorkIPStreams.actualize()
          }
        }
        consumer.close()
      } catch (e: Exception) {
        logger.error("MetaDataActualizer failed : ${e.message}")
      }
    }
  }

  override fun close() {
    isWork.value = false
  }
}