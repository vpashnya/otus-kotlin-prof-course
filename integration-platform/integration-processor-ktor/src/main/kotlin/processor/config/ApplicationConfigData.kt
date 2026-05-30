package ru.pvn.learning.processor.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

interface ApplicationConfig {
  val kafkaHosts: List<String>
  val kafkaGroupId: String
  val kafkaMetaActualizerTopic: String
  val ipStreamAppKtorUrl: String
  val ancientMonolithUrl: String

  fun createKafkaConsumer(): KafkaConsumer<String, String>
  fun createKafkaProducer(): KafkaProducer<String, String>
}

data class ApplicationConfigData(
  override val kafkaHosts: List<String>,
  override val kafkaGroupId: String,
  override val kafkaMetaActualizerTopic: String,
  override val ipStreamAppKtorUrl: String,
  override val ancientMonolithUrl: String,
) : ApplicationConfig {

  override fun createKafkaConsumer(): KafkaConsumer<String, String> {
    val props = Properties().apply {
      put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHosts)
      put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId)
      put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
      put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
    }
    return KafkaConsumer<String, String>(props)
  }

  override fun createKafkaProducer(): KafkaProducer<String, String> {
    val props = Properties().apply {
      put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHosts)
      put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
      put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }
    return KafkaProducer<String, String>(props)
  }
}

fun getApplicationConfig(): ApplicationConfig =
  ApplicationConfigData(
    kafkaHosts = getRequiredEnv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = getRequiredEnv("KAFKA_GROUP_ID"),
    kafkaMetaActualizerTopic = getRequiredEnv("KAFKA_META_ACTUALIZER_TOPIC"),
    ipStreamAppKtorUrl = getRequiredEnv("IP_STREAM_APP_KTOR_URL"),
    ancientMonolithUrl = getRequiredEnv("ANCIENT_MONOLITH_URL"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}