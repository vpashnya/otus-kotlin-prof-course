package ru.pvn.integration.platform

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties
import kotlin.String
import kotlin.jvm.java

data class ApplicationConfig(
  val mode: String,
  val kafkaHosts: List<String>,
  val kafkaGroupId: String,
  val kafkaIPStreamTopicIn: String,
  val kafkaIPStreamTopicOut: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = System.getenv("MODE"),
    kafkaHosts = System.getenv("KAFKA_HOSTS").split("\\s*[,; ]\\s*"),
    kafkaGroupId = System.getenv("KAFKA_GROUP_ID"),
    kafkaIPStreamTopicIn = System.getenv("KAFKA_IP_STREAM_TOPIC_V1_IN"),
    kafkaIPStreamTopicOut = System.getenv("KAFKA_IP_STREAM_TOPIC_V1_OUT"),
  )

