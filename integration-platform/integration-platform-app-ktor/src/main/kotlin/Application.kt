package ru.pvn.integration

import io.ktor.server.application.*
import ru.pvn.integration.plugins.initApplicationSettings

fun main(args: Array<String>) {
  io.ktor.server.tomcat.jakarta.EngineMain.main(args)
}

fun Application.module(appSettings: ApplicationSettings = initApplicationSettings()) {
  configureRouting(appSettings)
}
