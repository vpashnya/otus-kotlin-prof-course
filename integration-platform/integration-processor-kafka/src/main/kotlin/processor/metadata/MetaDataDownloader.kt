package ru.pvn.learning.processor.kafka.processor.metadata

import apiV1RequestSerialize
import apiV1ResponseDeserialize
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
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import kotlin.String

interface MetaDataDownloader {
  suspend fun download(): List<IPStreamRecord>
}

class MetaDataDownloaderImpl(
  private val ipStreamAppKtorUrl: String,
  private val httpClient: HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
  },
) : MetaDataDownloader {

  override suspend fun download(): List<IPStreamRecord> {
    val request = StreamAccessibleRequest(requestType = "accessible")
    val url = "${ipStreamAppKtorUrl}/accessible"
    val response: HttpResponse = httpClient.post(url) {
      method = HttpMethod.Post
      contentType(ContentType.Application.Json)
      setBody(apiV1RequestSerialize(request))
    }

    val ipStreamRecords =
      (apiV1ResponseDeserialize(response.body()) as StreamAccessibleResponse)
        .streams
        ?.let {
          it.asSequence()
            .mapNotNull { metaDataRecord ->
              IPStreamRecord(
                classShortName = metaDataRecord.classShortName?.lowercase() ?: return@mapNotNull null,
                methodShortName = metaDataRecord.methodShortName?.lowercase() ?: return@mapNotNull null,
                transportParams = metaDataRecord.transportParams?.lowercase() ?: return@mapNotNull null,
              )
            }
            .distinct()
            .toList()
        } ?: emptyList()

    return ipStreamRecords
  }

}