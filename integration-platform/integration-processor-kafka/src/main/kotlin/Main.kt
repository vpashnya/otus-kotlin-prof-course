package ru.pvn.learning.processor.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import ru.pvn.learning.processor.config.ApplicationConfig
import ru.pvn.learning.processor.config.ApplicationConfigData
import ru.pvn.learning.processor.config.ApplicationSettings
import ru.pvn.learning.processor.config.getApplicationConfig
import ru.pvn.learning.processor.config.initApplicationSettings
import ru.pvn.learning.processor.kafka.processor.handlers.IntegrationStreamsStarter
import ru.pvn.learning.processor.kafka.processor.handlers.IntegrationStreamsStarterImpl
import ru.pvn.learning.processor.kafka.processor.metadata.MetaDataActualizer
import ru.pvn.learning.processor.kafka.processor.metadata.MetaDataActualizerImpl
import ru.pvn.learning.processor.kafka.processor.metadata.MetaDataDownloader
import ru.pvn.learning.processor.kafka.processor.metadata.MetaDataDownloaderImpl
import kotlin.String
import kotlin.getValue

fun main() {
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
      single<IntegrationStreamsStarter> {
        IntegrationStreamsStarterImpl(
          config = get<ApplicationConfig>(),
          metadataDownloader = get<MetaDataDownloader>(),
        )
      }
      single<MetaDataActualizer> {
        MetaDataActualizerImpl(
          consumer = get<KafkaConsumer<String, String>>(named("METADATA_CONSUMER")),
          refreshTopic = (get<ApplicationConfig>() as ApplicationConfigData).kafkaMetaActualizerTopic,
          streamsStarter = get<IntegrationStreamsStarter>()
        )
      }
    }

  startKoin {
    modules(appModule)
  }

  val integrationStreamsStarter: IntegrationStreamsStarter by inject(IntegrationStreamsStarter::class.java)
  integrationStreamsStarter.restart()

  val metaDataActualizer: MetaDataActualizer by inject(MetaDataActualizer::class.java)
  metaDataActualizer.start()

}
