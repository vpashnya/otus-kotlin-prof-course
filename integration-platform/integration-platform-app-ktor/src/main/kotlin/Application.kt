package ru.pvn.integration.platform.ktor

import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.tomcat.jakarta.EngineMain.main(args)
}

fun Application.module(appSettings: ApplicationSettings = initApplicationSettings(getApplicationConfig())) {
  configureRouting(appSettings)
}
