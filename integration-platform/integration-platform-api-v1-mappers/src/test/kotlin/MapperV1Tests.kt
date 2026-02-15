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
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPExternalSystemId
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamFilter
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPWorkMode
import ru.pvn.learning.stubs.IPStubs

class MapperV1Tests {
  enum class BuildContextFromRequestCase(
    val request: IRequest,
    val context: IPContext,
  ) {
    CREATE(
      request = StreamCreateRequest(
        debug = StreamDebug(mode = StreamRequestDebugMode.STUB, stub = StreamRequestDebugStubs.SUCCESS),
        stream = StreamCreateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      ),
      context = IPContext(
        command = IPCommand.CREATE,
        workMode = IPWorkMode.STUB,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      )
    ),
    UPDATE(
      request = StreamUpdateRequest(
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = "12345"
        )
      ),
      context = IPContext(
        command = IPCommand.UPDATE,
        streamRequest = IPStream(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          id = IPStreamId("12345")
        ),
      )
    ),
    DELETE(
      request = StreamDeleteRequest(
        streamId = "12345"
      ),
      context = IPContext(
        command = IPCommand.DELETE,
        streamRequest = IPStream(id = IPStreamId("12345"))
      )
    ),
    READ(
      request = StreamReadRequest(
        streamId = "12345"
      ),
      context = IPContext(
        command = IPCommand.READ,
        streamRequest = IPStream(id = IPStreamId("12345")),
      )
    ),
    ENABLE(
      request = StreamEnableRequest(streamId = "12345"),
      context = IPContext(
        command = IPCommand.ENABLE,
        streamRequest = IPStream(id = IPStreamId("12345")),
      )
    ),
    DISABLE(
      request = StreamDisableRequest(streamId = "12345"),
      context = IPContext(
        command = IPCommand.DISABLE,
        streamRequest = IPStream(
          id = IPStreamId("12345")
        ),
      )
    ),
    SEARCH(
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        )
      ),
      context = IPContext(
        command = IPCommand.SEARCH, streamFilterRequest = IPStreamFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        )
      )
    ),
    ACCESSIBLE(
      request = StreamAccessibleRequest(
        externalSystemId = "333"
      ),
      context = IPContext(
        command = IPCommand.ACCESSIBLE, requesterExternalSystemId = IPExternalSystemId("333")
      )
    )
  }

  enum class BuildResponseFromContextCase(
    val context: IPContext,
    val response: IResponse,
  ) {
    CREATE(
      context = IPContext(
        command = IPCommand.CREATE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
      response = StreamCreateResponse(
        errors = listOf(Error(code = "A1", group = "G1", message = "Some message...")),
        stream = StreamResponseObject(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = "12345"
        )
      )
    ),
    UPDATE(
      context = IPContext(
        command = IPCommand.UPDATE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
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
      )
    ),
    READ(
      context = IPContext(
        command = IPCommand.READ,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
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
    ),
    DELETE(
      context = IPContext(
        command = IPCommand.DELETE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
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
    ),
    ENABLE(
      context = IPContext(
        command = IPCommand.ENABLE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
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

      ),
    DISABLE(
      context = IPContext(
        command = IPCommand.DISABLE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamResponse = IPStream(
          classShortName = "KRED_CORP",
          methodShortName = "PAYMENT_CALENDAR",
          transportParams = "some params ...",
          description = "some stream ...",
          active = false,
          id = IPStreamId("12345")
        )
      ),
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
    ),
    ACCESSIBLE(
      context = IPContext(
        command = IPCommand.ACCESSIBLE,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamsResponse = mutableListOf(
          IPStream(
            classShortName = "KRED_CORP",
            methodShortName = "PAYMENT_CALENDAR",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = IPStreamId("12345")
          ),
          IPStream(
            classShortName = "KRED_CORP",
            methodShortName = "SIGN",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = IPStreamId("12346")
          )
        )
      ),
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

      ),
    SEARCH(
      context = IPContext(
        command = IPCommand.SEARCH,
        errors = mutableListOf(IPError(code = "A1", group = "G1", message = "Some message...")),
        streamsResponse = mutableListOf(
          IPStream(
            classShortName = "KRED_CORP",
            methodShortName = "PAYMENT_CALENDAR",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = IPStreamId("12345")
          ),
          IPStream(
            classShortName = "KRED_CORP",
            methodShortName = "SIGN",
            transportParams = "some params ...",
            description = "some stream ...",
            active = false,
            id = IPStreamId("12346")
          )
        )
      ),
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
    );
  }

  @ParameterizedTest
  @EnumSource
  fun mapperV1FromTransportTest(case: BuildContextFromRequestCase) =
    assertEquals(
      IPContext().also { it.fromTransport(case.request) }, case.context
    )

  @ParameterizedTest
  @EnumSource
  fun mapperV1ToTransportTest(case: BuildResponseFromContextCase) =
    assertEquals(
      case.context.toTransport(), case.response
    )
}