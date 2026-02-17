import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.api.v1.models.Error
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.IResponse
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

class RequestV1SerializationTests {

  enum class RequestEqualJsonCase(
    val request: IRequest,
    val json: String,
  ) {
    STREAM_CREATE(
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
      json = """{"requestType":"create","requestType":null,"debug":{"mode":"stub","stub":"success"},"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС"}}"""
    ),
    STREAM_UPDATE(
      request = StreamUpdateRequest(
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = "12345"
        )
      ),
      json = """{"requestType":"update","requestType":null,"debug":null,"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС","id":"12345"}}"""
    ),
    STREAM_DELETE(
      request = StreamDeleteRequest(streamId = "12345"),
      json = """{"requestType":"delete","requestType":null,"debug":null,"streamId":"12345"}"""
    ),
    STREAM_READ(
      request = StreamReadRequest(streamId = "12345"),
      json = """{"requestType":"read","requestType":null,"debug":null,"streamId":"12345"}"""
    ),
    STREAM_ENABLE(
      request = StreamEnableRequest(streamId = "12345"),
      json = """{"requestType":"enable","requestType":null,"debug":null,"streamId":"12345"}"""
    ),
    STREAM_DISABLE(
      request = StreamDisableRequest(streamId = "12345"),
      json = """{"requestType":"disable","requestType":null,"debug":null,"streamId":"12345"}"""
    ),
    STREAM_SEARCH(
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        )
      ),
      json = """{"requestType":"search","requestType":null,"debug":null,"streamFilter":{"searchString":"classShorName = 'KREDS%' and methodShortName = '%'  ","classShortName":"DEPOSIT","methodShortName":"OEPN","active":false}}"""
    ),
    STREAM_ACCESSIBLE(
      request = StreamAccessibleRequest(),
      json = """{"requestType":"accessible","requestType":null,"debug":null,"externalSystemId":null}"""
    )
  }

  enum class ResponseEqualJsonCase(
    val response: IResponse,
    val json: String,
  ) {
    STREAM_CREATE(
      response = StreamCreateResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message..."))
      ),
      json = """{"responseType":"create","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_UPDATE(
      response = StreamUpdateResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message..."))
      ),
      json = """{"responseType":"update","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_READ(
      response = StreamReadResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message..."))
      ),
      json = """{"responseType":"read","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_SEARCH(
      response = StreamSearchResponse(
        streams = listOf(
          StreamResponseObject(
            classShortName = "KRED_CORP",
            methodShortName = "PAYMENT_CALENDAR",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = "12345"
          ), StreamResponseObject(
            classShortName = "KRED_CORP",
            methodShortName = "SIGN",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = "12346"
          )
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message..."))
      ),
      json = """{"responseType":"search","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"streams":[{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false},{"classShortName":"KRED_CORP","methodShortName":"SIGN","transportParams":"some params ...","description":"some stream ...","id":"12346","active":false}]}"""
    ),
    STREAM_DELETE(
      response = StreamDeleteResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message...")),
      ),
      json = """{"responseType":"delete","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_ENABLE(
      response = StreamEnableResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message...")),
      ),
      json = """{"responseType":"enable","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_DISABLE(
      response = StreamDisableResponse(
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message...")),
      ),
      json = """{"responseType":"disable","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"stream":{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false}}"""
    ),
    STREAM_ACCESSIBLE(
      response = StreamAccessibleResponse(
        streams = listOf(
          StreamResponseObject(
            classShortName = "KRED_CORP",
            methodShortName = "PAYMENT_CALENDAR",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = "12345"
          ), StreamResponseObject(
            classShortName = "KRED_CORP",
            methodShortName = "SIGN",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = "12346"
          )
        ),
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message..."))
      ),
      json = """{"responseType":"accessible","responseType":null,"result":null,"errors":[{"code":"A1","group":"G1","message":"Some message..."}],"streams":[{"classShortName":"KRED_CORP","methodShortName":"PAYMENT_CALENDAR","transportParams":"some params ...","description":"some stream ...","id":"12345","active":false},{"classShortName":"KRED_CORP","methodShortName":"SIGN","transportParams":"some params ...","description":"some stream ...","id":"12346","active":false}]}"""
    );
  }

  @ParameterizedTest
  @EnumSource
  fun serializeRequestTest(case: RequestEqualJsonCase) =
    assertEquals(
      apiV1RequestSerialize(case.request), case.json
    )

  @ParameterizedTest
  @EnumSource
  fun deserializeRequestTest(case: RequestEqualJsonCase) =
    assertEquals(
      apiV1RequestDeserialize(case.json), case.request
    )

  @ParameterizedTest
  @EnumSource
  fun serializeResponseTest(case: ResponseEqualJsonCase) =
    assertEquals(
      apiV1ResponseSerialize(case.response), case.json
    )

  @ParameterizedTest
  @EnumSource
  fun deserializeResponseTest(case: ResponseEqualJsonCase) =
    assertEquals(
      apiV1ResponseDeserialize(case.json), case.response
    )

}