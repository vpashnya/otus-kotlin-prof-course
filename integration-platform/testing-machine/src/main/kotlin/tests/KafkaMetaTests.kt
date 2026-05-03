package ru.pvn.learning.tests

import apiV1RequestSerialize
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.DisplayName
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import java.util.Properties
import kotlin.random.Random
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlin.jvm.java


class KafkaMetaTests {

  @DisplayName("Kafka - tests")
  @Test
  fun createTest() {
    val producerProps = Properties().apply {
      put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
      put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
    }

    val createRequest = StreamCreateRequest(
      requestType = "CREATE",
      stream = StreamCreateObject(
        classShortName = "KAFKATEST",
        methodShortName = "EXPORT2FNS",
        transportParams = "some transport",
        description = "Отправка информации в ФНС"
      )
    )


    KafkaProducer<String, String>(producerProps).use { producer ->
      val value = Random.nextInt()
      val record = ProducerRecord("ip.stream.v1.in", value.toString(), apiV1RequestSerialize(createRequest))
      producer.send(record) { metadata, exception ->
        if (exception == null)
          println("Sent: ${metadata.offset()}")
        else
          exception.printStackTrace()
      }
    }

    println("Finish kafka!")


    val consumerProps = Properties().apply {
      put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092") // Kafka broker address
      put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group") // Consumer group ID
      put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
      put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
      put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest") // Start from the beginning if no offset is committed
    }


    val consumer = KafkaConsumer<String, String>(consumerProps)

    consumer.subscribe(listOf("ip.stream.v1.out"))

    try {

        val records = consumer.poll(Duration.ofMillis(1000)) // Poll for records
        for (record in records) {
          println("Received record: offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()} in partition ${record.partition()}")
        }

    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      consumer.close()
    }
  }

}