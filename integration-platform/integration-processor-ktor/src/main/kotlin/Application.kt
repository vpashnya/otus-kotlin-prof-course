package ru.pvn.ktor.processor

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import ru.pvn.ktor.processor.processor.metadata.MetaDataActualizer
import ru.pvn.ktor.processor.processor.metadata.MetaDataActualizerImpl
import ru.pvn.ktor.processor.processor.metadata.MetaDataDownloader
import ru.pvn.ktor.processor.processor.metadata.MetaDataDownloaderImpl
import ru.pvn.learning.processor.config.ApplicationConfig
import ru.pvn.learning.processor.config.ApplicationConfigData
import ru.pvn.learning.processor.config.ApplicationSettings
import ru.pvn.learning.processor.config.getApplicationConfig
import ru.pvn.learning.processor.config.initApplicationSettings
import kotlin.getValue


fun main(args: Array<String>) {
  val appModule =
    module(createdAtStart = true) {
      single<ApplicationConfig> { getApplicationConfig() }
      single<ApplicationSettings> { initApplicationSettings(get()) }
      single<KafkaConsumer<String, String>>(named("METADATA_CONSUMER")) { get<ApplicationConfig>().createKafkaConsumer() }
      single<MetaDataDownloader> {
        MetaDataDownloaderImpl(
          ipStreamAppKtorUrl = (get<ApplicationConfig>() as ApplicationConfigData).ipStreamAppKtorUrl
        )
      }
      single<MetaDataActualizer> {
        MetaDataActualizerImpl(
          consumer = get<KafkaConsumer<String, String>>(named("METADATA_CONSUMER")),
          refreshTopic = (get<ApplicationConfig>() as ApplicationConfigData).kafkaMetaActualizerTopic,
        )
      }
      single<HttpClient> {
        HttpClient(CIO) {
          install(ContentNegotiation) {
            json()
          }
        }
      }
    }

  startKoin {
    modules(appModule)
  }

  val metaDataActualizer: MetaDataActualizer by inject(MetaDataActualizer::class.java)
  metaDataActualizer.start()

  io.ktor.server.tomcat.jakarta.EngineMain.main(args)
}
