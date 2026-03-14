package ru.pvn.integration

import ru.pvn.integration.platform.ktor.Mode.*
import ru.pvn.integration.platform.ktor.ApplicationSettings
import IPStreamProcessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.body
import io.ktor.serialization.jackson.jackson
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.testing.testApplication
import io.ktor.http.*
import io.ktor.client.request.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.api.v1.models.Error
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.IResponse
import ru.pvn.integration.platform.api.v1.models.ResponseResult
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamCreateResponse
import ru.pvn.integration.platform.api.v1.models.StreamDebug
import ru.pvn.integration.platform.api.v1.models.StreamDeleteRequest
import ru.pvn.integration.platform.api.v1.models.StreamDeleteResponse
import ru.pvn.integration.platform.api.v1.models.StreamDisableRequest
import ru.pvn.integration.platform.api.v1.models.StreamDisableResponse
import ru.pvn.integration.platform.api.v1.models.StreamEnableRequest
import ru.pvn.integration.platform.api.v1.models.StreamEnableResponse
import ru.pvn.integration.platform.api.v1.models.StreamReadRequest
import ru.pvn.integration.platform.api.v1.models.StreamReadResponse
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugMode
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugStubs
import ru.pvn.integration.platform.api.v1.models.StreamResponseObject
import ru.pvn.integration.platform.api.v1.models.StreamSearchFilter
import ru.pvn.integration.platform.api.v1.models.StreamSearchRequest
import ru.pvn.integration.platform.api.v1.models.StreamSearchResponse
import ru.pvn.integration.platform.api.v1.models.StreamUpdateObject
import ru.pvn.integration.platform.api.v1.models.StreamUpdateRequest
import ru.pvn.integration.platform.api.v1.models.StreamUpdateResponse
import ru.pvn.integration.platform.ktor.module
import kotlin.test.assertEquals

class ApplicationV1StubApiTests {
  enum class RequestCases(
    val route: String,
    val request: IRequest,
    val response: IResponse,
  ) {
    STREAM_CREATE_NORMAL(
      route = "create",
      request = StreamCreateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        ),
        stream = StreamCreateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      ),
      response = StreamCreateResponse(
        result = ResponseResult.SUCCESS,
        stream = StreamResponseObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          active = false,
          id = "999"
        )
      )
    ),
    STREAM_CREATE_ERROR(
      route = "create",
      request = StreamCreateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_DESCRIPTION
        ),
        stream = StreamCreateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      ),
      response = StreamCreateResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "CREATE_ERROR", message = "неверное описание"))
      )
    ),
    STREAM_UPDATE_NORMAL(
      route = "update",
      request = StreamUpdateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        ),
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = "12345",
        )
      ),
      response = StreamUpdateResponse(
        result = ResponseResult.SUCCESS,
        stream = StreamResponseObject
          (
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = "12345",
          active = false
        )
      )
    ),
    STREAM_UPDATE_ERROR(
      route = "update",
      request = StreamUpdateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_DESCRIPTION
        ),
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = "12345",
        )
      ),
      response = StreamUpdateResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "UPDATE_ERROR", message = "неверное описание"))
      )
    ),
    STREAM_DELETE_NORMAL(
      route = "delete",
      request = StreamDeleteRequest(
        streamId = "12345",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamDeleteResponse(
        stream = StreamResponseObject(
          id = "12345",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_DELETE_ERROR(
      route = "delete",
      request = StreamDeleteRequest(
        streamId = "12345",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.CANNOT_DELETE
        )
      ),
      response = StreamDeleteResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
      )
    ),
    STREAM_READ_NORMAL(
      route = "read",
      request = StreamReadRequest(
        streamId = "12345", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamReadResponse(
        stream = StreamResponseObject(
          id = "12345",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_READ_ERROR(
      route = "read",
      request = StreamReadRequest(
        streamId = "123",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.NOT_FOUND
        )
      ),
      response = StreamReadResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
      )
    ),
    STREAM_ENABLE_NORMAL(
      route = "enable",
      request = StreamEnableRequest(
        streamId = "456", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamEnableResponse(
        stream = StreamResponseObject(
          id = "456",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = true
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_ENABLE_ERROR(
      route = "enable",
      request = StreamEnableRequest(
        streamId = "789", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_ID
        )
      ),
      response = StreamEnableResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "ENABLE_ERROR", message = "ошибка включения"))
      )
    ),
    STREAM_DISABLE_NORMAL(
      route = "disable",
      request = StreamDisableRequest(
        streamId = "456", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamDisableResponse(
        stream = StreamResponseObject(
          id = "456",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_DISABLE_ERROR(
      route = "disable",
      request = StreamDisableRequest(
        streamId = "789", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_ID
        )
      ),
      response = StreamDisableResponse(
        stream = StreamResponseObject("", "", "", "", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "DISABLE_ERROR", message = "ошибка отключения"))
      )
    ),
    STREAM_SEARCH_NORMAL(
      route = "search",
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        ), debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamSearchResponse(
        streams =
          listOf(
            StreamResponseObject(
              id = "1",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              active = false
            ), StreamResponseObject(
              id = "2",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              active = false
            )
          ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_SEARCH_ERROR(
      route = "search",
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        ), debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_SEARCH_STRING
        )
      ),
      response = StreamSearchResponse(
        result = ResponseResult.ERROR,
        streams = emptyList(),
        errors = listOf(Error(code = "999", group = "SEARCH_ERROR", message = "ошибка поиска"))
      )
    ),
    STREAM_ACCESSIBLE(
      route = "accessible",
      request = StreamAccessibleRequest(
        externalSystemId = "11",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamAccessibleResponse(
        streams =
          listOf(
            StreamResponseObject(
              id = "1",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              active = false
            ), StreamResponseObject(
              id = "2",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              active = false
            )
          ),
        result = ResponseResult.SUCCESS,
      )
    ),
  }

  @ParameterizedTest
  @EnumSource
  fun routeTest(testCase: RequestCases) = testApplication {
    application {
      module(ApplicationSettings(mode = STUB, ipStreamProcessor = IPStreamProcessor()))
    }

    val client = createClient {
      install(ContentNegotiation) {
        jackson {
          disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
          enable(SerializationFeature.INDENT_OUTPUT)
          writerWithDefaultPrettyPrinter()
        }
      }
    }

    val response = client.post("/v1/ip/stream/${testCase.route}") {
      contentType(ContentType.Application.Json)
      setBody(testCase.request)
    }
    assertEquals(response.body(), testCase.response)
  }

}
