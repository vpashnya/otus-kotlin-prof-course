package ru.pvn.learning.config

import org.apache.kafka.clients.consumer.Consumer
import org.slf4j.Logger
import java.time.Duration

data class TopicPair(
  val incoming: String,
  val outgoing: String,
)


fun receiveFromTopic(consumer: Consumer<String, String>, topic: String, logger: Logger): List<String> =
  buildList {
    try {
      var needRead = true
      while (needRead) {
        repeat(15) {
          val records = consumer.poll(Duration.ofMillis(200)) // Poll for records
          for (record in records) {
            add(record.value())
          }
          needRead = !records.isEmpty || consumer.assignment().isEmpty()
        }
      }

    } catch (e: Exception) {
      logger.info("$!!!error!!! ${e.message}")
    }
  }


