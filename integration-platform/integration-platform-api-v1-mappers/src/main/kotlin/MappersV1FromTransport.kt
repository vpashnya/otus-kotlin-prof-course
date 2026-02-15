import exceptions.UnknownRequestClass
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamDebug
import ru.pvn.integration.platform.api.v1.models.StreamDeleteRequest
import ru.pvn.integration.platform.api.v1.models.StreamDisableRequest
import ru.pvn.integration.platform.api.v1.models.StreamEnableRequest
import ru.pvn.integration.platform.api.v1.models.StreamReadRequest
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugMode
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugStubs
import ru.pvn.integration.platform.api.v1.models.StreamSearchRequest
import ru.pvn.integration.platform.api.v1.models.StreamUpdateObject
import ru.pvn.integration.platform.api.v1.models.StreamUpdateRequest
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPExternalSystemId
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamFilter
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPWorkMode
import ru.pvn.learning.stubs.IPStubs
import kotlin.String

fun IPContext.fromTransport(request: IRequest) =
  when (request) {
    is StreamCreateRequest -> fromTransport(request)
    is StreamReadRequest -> fromTransport(request)
    is StreamUpdateRequest -> fromTransport(request)
    is StreamDeleteRequest -> fromTransport(request)
    is StreamSearchRequest -> fromTransport(request)
    is StreamAccessibleRequest -> fromTransport(request)
    is StreamEnableRequest -> fromTransport(request)
    is StreamDisableRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
  }

fun IPContext.fromTransport(request: StreamCreateRequest) {
  command = IPCommand.CREATE
  request.stream?.let { stream -> streamRequest = stream.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamDeleteRequest) {
  command = IPCommand.DELETE
  request.streamId?.let { streamId -> streamRequest = streamId.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamReadRequest) {
  command = IPCommand.READ
  request.streamId?.let { streamId -> streamRequest = streamId.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamUpdateRequest) {
  command = IPCommand.UPDATE
  request.stream?.let { stream -> streamRequest = stream.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamSearchRequest) {
  command = IPCommand.SEARCH
  request.streamFilter?.let { streamFilter ->
    streamFilterRequest =
      IPStreamFilter(
        searchString = streamFilter.searchString ?: "",
        classShortName = streamFilter.classShortName ?: "",
        methodShortName = streamFilter.methodShortName ?: "",
        active = streamFilter.active ?: false
      )
  }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}


fun IPContext.fromTransport(request: StreamAccessibleRequest) {
  command = IPCommand.ACCESSIBLE
  request.externalSystemId?.let { externalSystemId -> requesterExternalSystemId = IPExternalSystemId(externalSystemId) }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamEnableRequest) {
  command = IPCommand.ENABLE
  request.streamId?.let { streamId -> streamRequest = streamId.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun IPContext.fromTransport(request: StreamDisableRequest) {
  command = IPCommand.DISABLE
  request.streamId?.let { streamId -> streamRequest = streamId.toInternal() }
  workMode = request.debug.transportToWorkMode()
  stubCase = request.debug.transportToStubCase()
}

fun String.toInternal() = IPStream(
  id = IPStreamId(this)
)

fun StreamCreateObject.toInternal() = IPStream(
  description = description ?: "",
  classShortName = classShortName ?: "",
  methodShortName = methodShortName ?: "",
  transportParams = transportParams ?: "",
)

fun StreamUpdateObject.toInternal() = IPStream(
  description = description ?: "",
  classShortName = classShortName ?: "",
  methodShortName = methodShortName ?: "",
  transportParams = transportParams ?: "",
  id = id?.let { id -> IPStreamId(id) } ?: IPStreamId.NONE
)

private fun StreamDebug?.transportToWorkMode(): IPWorkMode = when (this?.mode) {
  StreamRequestDebugMode.PROD -> IPWorkMode.PROD
  StreamRequestDebugMode.TEST -> IPWorkMode.TEST
  StreamRequestDebugMode.STUB -> IPWorkMode.STUB
  null -> IPWorkMode.PROD
}

private fun StreamDebug?.transportToStubCase(): IPStubs = when (this?.stub) {
  StreamRequestDebugStubs.SUCCESS -> IPStubs.SUCCESS
  StreamRequestDebugStubs.NOT_FOUND -> IPStubs.NOT_FOUND
  StreamRequestDebugStubs.BAD_ID -> IPStubs.BAD_ID
  StreamRequestDebugStubs.BAD_DESCRIPTION -> IPStubs.BAD_DESCRIPTION
  StreamRequestDebugStubs.CANNOT_DELETE -> IPStubs.CANNOT_DELETE
  StreamRequestDebugStubs.BAD_SEARCH_STRING -> IPStubs.BAD_SEARCH_STRING
  null -> IPStubs.NONE
}
