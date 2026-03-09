package ru.pvn.integration.platform

import IPStreamProcessor


data class ApplicationSettings(
  val mode: Mode,
  val ipStreamProcessor: IPStreamProcessor,
)

enum class Mode {
  PROD, TEST, STUB
}

fun initApplicationSettings(applicationConfig: ApplicationConfig) = ApplicationSettings(
  mode = Mode.valueOf(applicationConfig.mode),
  ipStreamProcessor = IPStreamProcessor()
)