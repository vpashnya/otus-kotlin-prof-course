package ru.pvn.integration.platform.ktor

import IPStreamProcessor


data class ApplicationSettings(
  val mode: Mode,
  val ipStreamProcessor: IPStreamProcessor,
)

enum class Mode {
  PROD, TEST, STUB
}

fun initApplicationSettings() = ApplicationSettings(
  mode = Mode.PROD,
  ipStreamProcessor = IPStreamProcessor()
)