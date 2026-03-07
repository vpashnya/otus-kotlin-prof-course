package ru.pvn.integration

import apiV1Mapper
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import ru.pvn.integration.v1.v1IP


fun Application.configureRouting() {
  routing {
    route("v1") {
      install(ContentNegotiation) {
        jackson {
          setConfig(apiV1Mapper.serializationConfig)
          setConfig(apiV1Mapper.deserializationConfig)
        }
      }
      v1IP()
    }
  }
}

