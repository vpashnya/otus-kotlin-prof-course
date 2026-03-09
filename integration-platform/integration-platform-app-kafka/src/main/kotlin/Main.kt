package ru.pvn.integration.platform

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import ru.pvn.integration.platform.ApplicationSettings

fun main() {
  val applicationConfig = getApplicationConfig();
  val applicationSettings = initApplicationSettings(applicationConfig)

  val ipStreamConsumer = applicationConfig.createIPStreamConsumer()
  val ipStreamProducer = applicationConfig.createIPStreamProducer()
  val ipStreamTopics = applicationConfig.createIPStreamTopicPair()

  val ipStreamHandler = IPStreamHandler(
    applicationSettings = applicationSettings,
    consumer = ipStreamConsumer,
    producer = ipStreamProducer,
    topics = ipStreamTopics
  )

  ipStreamHandler.start()

}