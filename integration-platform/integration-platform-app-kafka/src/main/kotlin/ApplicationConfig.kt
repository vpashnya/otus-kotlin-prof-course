package ru.pvn.integration.platform.kafka

data class ApplicationConfig(
  val mode: String,
  val kafkaHosts: List<String>,
  val kafkaGroupId: String,
  val kafkaIPStreamTopicIn: String,
  val kafkaIPStreamTopicOut: String,
  val pgUrl: String,
  val pgUser: String,
  val pgPassword: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = getRequiredEnv("MODE"),
    kafkaHosts = getRequiredEnv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = getRequiredEnv("KAFKA_GROUP_ID"),
    kafkaIPStreamTopicIn = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_IN"),
    kafkaIPStreamTopicOut = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_OUT"),
    pgUrl = getRequiredEnv("PG_URL"),
    pgUser = getRequiredEnv("PG_USER"),
    pgPassword = getRequiredEnv("PG_PASSWORD"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}