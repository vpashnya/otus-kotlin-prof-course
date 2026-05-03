package ru.pvn.learning.tests

import apiV1RequestSerialize
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest


class KtorMetaTests {

  companion object {
    val client = HttpClient(CIO) {
      install(ContentNegotiation) {
        json()
      }
    }

    @JvmStatic
    @AfterAll
    fun closeClient() {
      client.close()
    }
  }

  @DisplayName("Ktor - tests")
  @Test
  fun createTest() {
    val createRequest = StreamCreateRequest(
      requestType = "CREATE",
      stream = StreamCreateObject(
        classShortName = "KTORTEST",
        methodShortName = "EXPORT2FNS",
        transportParams = "some transport",
        description = "Отправка информации в ФНС"
      )
    )

    runBlocking {
      val response: HttpResponse = client.post("http://localhost:8888/v1/ip/stream/create") {
        method = HttpMethod.Post
        contentType(ContentType.Application.Json)
        setBody(apiV1RequestSerialize(createRequest))
      }
      println(response.body<String>())
    }
  }





}