package ru.pvn.ktor.processor

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject
import ru.pvn.ktor.processor.processor.metadata.IPStreamRecord
import ru.pvn.ktor.processor.processor.metadata.lowercase
import ru.pvn.learning.processor.config.ApplicationConfig

val mapper = JsonMapper.builder().run {
  enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL).build()
}

fun Application.configureRouting() {
  WorkIPStreams.actualize()
  val httpClient: HttpClient by inject(HttpClient::class.java)
  val applicationConfigData: ApplicationConfig by inject(ApplicationConfig::class.java)

  routing {
    install(ContentNegotiation) {
      jackson {
        setConfig(mapper.serializationConfig)
        setConfig(mapper.deserializationConfig)
      }

      post("integration") {
        val messageToAncient = call.receive<MessageToAncient>()
        if (messageToAncient.ipStream.lowercase() in WorkIPStreams.items) {

          val monolithResponse: HttpResponse = httpClient.post(applicationConfigData.ancientMonolithUrl) {
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            setBody(messageToAncient.message)
          }
          call.respond(monolithResponse.body<String>())

        } else {
          call.respond("${messageToAncient.ipStream} not work!")

        }
      }
    }
  }
}

data class MessageToAncient(
  val ipStream: IPStreamRecord,
  val message: String,
)