package ru.pvn.integration.platform.kafka

import IPStreamProcessor
import ru.pvn.integration.platform.repo.inmemory.RepoStreamInMemory
import ru.pvn.learning.repo.IRepoStream


data class ApplicationSettings(
  val mode: Mode,
  val ipStreamProcessor: IPStreamProcessor,
  val ipStreamRepo: IRepoStream
)

enum class Mode {
  PROD, TEST, STUB
}

fun initApplicationSettings(applicationConfig: ApplicationConfig) = ApplicationSettings(
  mode = Mode.valueOf(applicationConfig.mode),
  ipStreamProcessor = IPStreamProcessor(),
  ipStreamRepo = RepoStreamInMemory()
)