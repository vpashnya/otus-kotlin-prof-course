package ru.pvn.integration.platform.kafka

import kotlin.String

data class ApplicationConfig(
  val mode: String,
  val kafkaHosts: List<String>,
  val kafkaGroupId: String,
  val kafkaIPStreamTopicIn: String,
  val kafkaIPStreamTopicOut: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = getRequiredEnv("MODE"),
    kafkaHosts = getRequiredEnv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = getRequiredEnv("KAFKA_GROUP_ID"),
    kafkaIPStreamTopicIn = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_IN"),
    kafkaIPStreamTopicOut = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_OUT"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}