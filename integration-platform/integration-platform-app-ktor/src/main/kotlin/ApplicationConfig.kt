package ru.pvn.integration.platform.ktor

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

interface ApplicationConfig {
  fun createKafkaProducer(): KafkaProducer<String, String>
}

data class ApplicationConfigData(
  val mode: String,
  val kafkaHosts: List<String>,
  val kafkaGroupId: String,
  val kafkaMetaActualizerTopic: String,
  val pgUrl: String,
  val pgUser: String,
  val pgPassword: String,
  val pgMaximumPoolSize: String,
  val pgMinimumIdle: String,
  val pgIdleTimeout: String,
  val pgConnectionTimeout: String,
) : ApplicationConfig {
  override fun createKafkaProducer(): KafkaProducer<String, String> {
    val props = Properties().apply {
      put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHosts)
      put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
      put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }
    return KafkaProducer<String, String>(props)
  }
}

fun getApplicationConfig() =
  ApplicationConfigData(
    mode = getRequiredEnv("MODE"),
    kafkaHosts = getRequiredEnv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = getRequiredEnv("KAFKA_GROUP_ID"),
    kafkaMetaActualizerTopic = getRequiredEnv("KAFKA_META_ACTUALIZER_TOPIC"),
    pgUrl = getRequiredEnv("PG_URL"),
    pgUser = getRequiredEnv("PG_USER"),
    pgPassword = getRequiredEnv("PG_PASSWORD"),
    pgMaximumPoolSize = getRequiredEnv("PG_MAXIMUM_POOL_SIZE"),
    pgMinimumIdle = getRequiredEnv("PG_MINIMUM_IDLE"),
    pgIdleTimeout = getRequiredEnv("PG_IDLE_TIMEOUT"),
    pgConnectionTimeout = getRequiredEnv("PG_CONNECTION_TIMEOUT"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}