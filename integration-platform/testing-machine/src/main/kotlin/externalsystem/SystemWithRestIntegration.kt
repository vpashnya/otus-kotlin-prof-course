package ru.pvn.learning.tests

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamDisableRequest
import ru.pvn.integration.platform.api.v1.models.StreamEnableRequest
import ru.pvn.learning.config.ApplicationConfigData
import ru.pvn.learning.testing.machine.externalsystem.MonolithClasses
import ru.pvn.learning.testing.machine.externalsystem.MonolithMethods
import ru.pvn.learning.testing.machine.externalsystem.RestTransportParams
import kotlin.collections.forEach
import kotlin.random.Random


class SystemWithRestIntegration(
  private val applicationConfigData: ApplicationConfigData,
  private val logger: Logger = LoggerFactory.getLogger("SystemWithRestIntegration"),
  private val httpClient: HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
  },
) : AutoCloseable {
  fun sendFillingMetadataToRest(): String {
    val streams = buildList {
      MonolithClasses.entries.forEach { cl ->
        MonolithMethods.entries.forEach { mth ->
          if (Random.nextInt(10) < 3) {
            add(IntegrationStream(cl, mth, RestTransportParams.entries.random()))
          }
        }
      }
    }

    val responds = runBlocking {
      streams.map { stream ->
        val creteRequest = StreamCreateRequest(
          requestType = "CREATE",
          stream = StreamCreateObject(
            classShortName = stream.mClass.name,
            methodShortName = stream.mMethod.name,
            transportParams = stream.mTransportParams.name,
            description = "Описание потока ${stream.mTransportParams.name}.${stream.mClass.name}.${stream.mMethod.name}"
          )
        )

        val response = httpClient.post("${applicationConfigData.urlIpStreamApplication}/v1/ip/stream/create") {
          method = HttpMethod.Post
          contentType(ContentType.Application.Json)
          setBody(apiV1RequestSerialize(creteRequest))
        }
        response.body<String>()
      }
    }

    val respondText = StringBuilder()
    respondText.append("created random streams:\n")
    responds.forEach { respond ->
      respondText.append("$respond \n")
    }

    return respondText.toString()
  }

  fun enableRandomStreams(): String = runBlocking {
    val streamsMetadata = getFullMetadata()
    val responds = streamsMetadata
      .streams
      ?.filter { it.transportParams?.contains("rest") == true && Random.nextBoolean() == true }
      ?.map { stream ->
        val response = httpClient.post("${applicationConfigData.urlIpStreamApplication}/v1/ip/stream/enable") {
          method = HttpMethod.Post
          contentType(ContentType.Application.Json)
          setBody(apiV1RequestSerialize(StreamEnableRequest(streamId = stream.id, version = stream.version)))
        }
        response.body<String>()
      }

    val respondText = StringBuilder()
    respondText.append("enabled random streams:\n")
    responds?.forEach { respond ->
      respondText.append("$respond \n")
    }

    return@runBlocking respondText.toString()
  }

  fun disableAllStreams(): String = runBlocking {
    val streamsMetadata = getFullMetadata()
    val responds = streamsMetadata
      .streams
      ?.filter { it.transportParams?.contains("rest") == true }
      ?.map { stream ->
        val response = httpClient.post("${applicationConfigData.urlIpStreamApplication}/v1/ip/stream/disable") {
          method = HttpMethod.Post
          contentType(ContentType.Application.Json)
          setBody(apiV1RequestSerialize(StreamDisableRequest(streamId = stream.id, version = stream.version)))
        }
        response.body<String>()
      }

    val respondText = StringBuilder()
    respondText.append("disabled streams:\n")
    responds?.forEach { respond ->
      respondText.append("$respond \n")
    }

    return@runBlocking respondText.toString()
  }


  fun sendSyntheticDataForStreams(): String {
    val streamsMetadata = getFullMetadata()

    val respondText = StringBuilder()
    respondText.append("send to random streams:\n")

    TODO("ДОПИЛИТЬ ОТПРАВКУ ДАННЫХ В KTOR-PROCESSOR")

    return respondText.toString()
  }


  private fun getFullMetadata(): StreamAccessibleResponse = runBlocking {
    val response = httpClient.post("${applicationConfigData.urlIpStreamApplication}/v1/ip/stream/accessible") {
      method = HttpMethod.Post
      contentType(ContentType.Application.Json)
      setBody(apiV1RequestSerialize(StreamAccessibleRequest()))
    }

    return@runBlocking (apiV1ResponseDeserialize(response.body<String>()) as StreamAccessibleResponse)
  }

  override fun close() {
    httpClient.close()
  }

  data class IntegrationStream(
    val mClass: MonolithClasses,
    val mMethod: MonolithMethods,
    val mTransportParams: RestTransportParams,
  )
}