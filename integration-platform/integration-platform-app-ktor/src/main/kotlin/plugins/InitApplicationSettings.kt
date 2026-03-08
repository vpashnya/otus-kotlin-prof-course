package ru.pvn.integration.plugins

import IPStreamProcessor
import ru.pvn.integration.ApplicationSettings
import ru.pvn.integration.Mode.*

fun initApplicationSettings() = ApplicationSettings(
  mode = PROD,
  ipStreamProcessor = IPStreamProcessor()
)