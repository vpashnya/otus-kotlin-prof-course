package ru.pvn.learning.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

interface ApplicationConfig {
  fun createKafkaConsumer(): KafkaConsumer<String, String>
  fun createKafkaProducer(): KafkaProducer<String, String>
  fun createIPStreamTopicPair(): TopicPair
}

data class ApplicationConfigData(
  val kafkaHosts: List<String>,
  val kafkaGroupId: String,
  val kafkaIPStreamTopicIn: String,
  val kafkaIPStreamTopicOut: String,
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

  override fun createIPStreamTopicPair() = TopicPair(
    incoming = kafkaIPStreamTopicIn,
    outgoing = kafkaIPStreamTopicOut
  )
}

fun getApplicationConfig(): ApplicationConfig =
  ApplicationConfigData(
    kafkaHosts = getRequiredEnv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = getRequiredEnv("KAFKA_GROUP_ID"),
    kafkaIPStreamTopicIn = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_IN"),
    kafkaIPStreamTopicOut = getRequiredEnv("KAFKA_IP_STREAM_TOPIC_V1_OUT"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}