package ru.pvn.learning.processor.config

import org.apache.kafka.clients.consumer.Consumer

data class ApplicationSettings(
  val metaDataActualizerConsumer: Consumer<String, String>,
)

fun initApplicationSettings(applicationConfig: ApplicationConfig): ApplicationSettings {
  applicationConfig as ApplicationConfigData

  return ApplicationSettings(
    metaDataActualizerConsumer = applicationConfig.createKafkaConsumer()
  )
}
