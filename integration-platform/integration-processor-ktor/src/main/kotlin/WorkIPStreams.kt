package ru.pvn.ktor.processor

import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import org.slf4j.LoggerFactory
import ru.pvn.ktor.processor.processor.metadata.IPStreamRecord
import ru.pvn.ktor.processor.processor.metadata.MetaDataDownloader
import java.util.concurrent.ConcurrentHashMap
import kotlin.getValue

object WorkIPStreams {
  val items: MutableSet<IPStreamRecord> = ConcurrentHashMap.newKeySet()
  val logger = LoggerFactory.getLogger(WorkIPStreams::class.java)

  fun actualize(): Unit = runBlocking {
    logger.info("actualize active ip streams")

    val metaDataDownloader: MetaDataDownloader by inject(MetaDataDownloader::class.java)
    val newStreams = metaDataDownloader.download()
    items.retainAll(newStreams)
    items.addAll(newStreams)
  }
}