package ru.pvn.integration.platform.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.koin.dsl.module
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.inject

fun main() {

  val appModule = module(createdAtStart = true)  {
    single<ApplicationConfig> { getApplicationConfig() }
    single<ApplicationSettings> { initApplicationSettings(get()) }
    single<KafkaConsumer<String, String>>(named("IP_STREAM_CONSUMER")) { get<ApplicationConfig>().createIPStreamConsumer() }
    single<KafkaProducer<String, String>>((named("IP_STREAM_PRODUCER"))) { get<ApplicationConfig>().createIPStreamProducer() }
    single<TopicPair>(named("IP_STREAM_TOPICS")) { get<ApplicationConfig>().createIPStreamTopicPair() }
    single {
      IPStreamHandler(
        applicationSettings = get(),
        consumer = get<KafkaConsumer<String, String>>(named("IP_STREAM_CONSUMER")),
        producer = get<KafkaProducer<String, String>>(named("IP_STREAM_PRODUCER")),
        topics = get<TopicPair>(named("IP_STREAM_TOPICS"))
      )
    }
  }

  startKoin {
    modules(appModule)
  }

  val ipStreamHandler : IPStreamHandler by inject(IPStreamHandler::class.java)
  ipStreamHandler.start()
}