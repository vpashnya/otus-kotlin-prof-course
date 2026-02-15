import ru.pvn.integration.platform.api.v1.models.IResponse
import ru.pvn.integration.platform.api.v1.models.ResponseResult
import ru.pvn.integration.platform.api.v1.models.StreamCreateResponse
import ru.pvn.integration.platform.api.v1.models.Error
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import ru.pvn.integration.platform.api.v1.models.StreamDeleteResponse
import ru.pvn.integration.platform.api.v1.models.StreamDisableResponse
import ru.pvn.integration.platform.api.v1.models.StreamEnableResponse
import ru.pvn.integration.platform.api.v1.models.StreamReadResponse
import ru.pvn.integration.platform.api.v1.models.StreamResponseObject
import ru.pvn.integration.platform.api.v1.models.StreamSearchResponse
import ru.pvn.integration.platform.api.v1.models.StreamUpdateResponse
import ru.pvn.learning.IPContext
import ru.pvn.learning.exceptions.UnknownIPCommand
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId

fun IPContext.toTransport(): IResponse =
  when (command) {
    IPCommand.CREATE -> toTransportCreate()
    IPCommand.READ -> toTransportRead()
    IPCommand.UPDATE -> toTransportUpdate()
    IPCommand.DELETE -> toTransportDelete()
    IPCommand.ENABLE -> toTransportEnable()
    IPCommand.DISABLE -> toTransportDisable()
    IPCommand.SEARCH -> toTransportSearch()
    IPCommand.ACCESSIBLE -> toTransportAccessible()
    IPCommand.NONE -> throw UnknownIPCommand(command)
  }

fun IPContext.toTransportCreate() = StreamCreateResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportRead() = StreamReadResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportUpdate() = StreamUpdateResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportDelete() = StreamDeleteResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportEnable() = StreamEnableResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportDisable() = StreamDisableResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  stream = streamResponse.toTransport(),
)

fun IPContext.toTransportSearch() = StreamSearchResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  streams = streamsResponse.map { it.toTransport() }
)

fun IPContext.toTransportAccessible() = StreamAccessibleResponse(
  result = state.toResult(),
  errors = errors.toTransportErrors(),
  streams = streamsResponse.map { it.toTransport() }
)

fun IPState.toResult(): ResponseResult? = when (this) {
  IPState.RUNNING -> ResponseResult.SUCCESS
  IPState.FAILING -> ResponseResult.ERROR
  IPState.FINISHING -> ResponseResult.SUCCESS
  IPState.NONE -> null
}

private fun List<IPError>.toTransportErrors(): List<Error>? = this
  .map { it.toTransport() }
  .toList()
  .takeIf { it.isNotEmpty() }

private fun IPError.toTransport() = Error(
  code = code.takeIf { it.isNotBlank() },
  group = group.takeIf { it.isNotBlank() },
  message = message.takeIf { it.isNotBlank() },
)

fun IPStream.toTransport(): StreamResponseObject = StreamResponseObject(
  id = id.toTransport(),
  classShortName = classShortName,
  methodShortName = methodShortName,
  transportParams = transportParams,
  description = description,
  active = active
)

internal fun IPStreamId.toTransport() = takeIf { it != IPStreamId.NONE }?.asString()
