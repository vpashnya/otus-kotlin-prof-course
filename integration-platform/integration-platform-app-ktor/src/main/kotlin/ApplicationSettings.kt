package ru.pvn.integration

import IPStreamProcessor

data class ApplicationSettings(
  val mode: Mode,
  val ipStreamProcessor: IPStreamProcessor,
)

enum class Mode {
  PROD, TEST, STUB
}

