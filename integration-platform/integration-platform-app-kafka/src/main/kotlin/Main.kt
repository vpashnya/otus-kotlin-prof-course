package ru.pvn.integration.platform.kafka

fun main() {
  val applicationConfig = getApplicationConfig()
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