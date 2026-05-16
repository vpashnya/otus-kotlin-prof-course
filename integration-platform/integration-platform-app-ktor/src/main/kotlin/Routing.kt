package ru.pvn.integration.platform.ktor

import io.ktor.http.HttpStatusCode.Companion.OK
import apiV1Mapper
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import ru.pvn.integration.ktor.v1.v1IP


fun Application.configureRouting(settings: ApplicationSettings) {
  routing {
    route("health") {
      get {
        if (settings.ipStreamRepo.isOk) {
          call.respond(OK)
        }
      }
    }
    route("v1") {
      install(ContentNegotiation) {
        jackson {
          setConfig(apiV1Mapper.serializationConfig)
          setConfig(apiV1Mapper.deserializationConfig)
        }
      }
      v1IP(settings)
    }
  }
}

