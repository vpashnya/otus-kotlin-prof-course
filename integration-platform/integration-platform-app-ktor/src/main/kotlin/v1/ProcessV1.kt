package ru.pvn.integration.v1

import fromTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import ru.pvn.integration.ApplicationSettings
import ru.pvn.integration.Mode
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.IResponse
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamCreateResponse
import ru.pvn.integration.platform.api.v1.models.StreamDeleteRequest
import ru.pvn.integration.platform.api.v1.models.StreamDeleteResponse
import ru.pvn.integration.platform.api.v1.models.StreamDisableRequest
import ru.pvn.integration.platform.api.v1.models.StreamDisableResponse
import ru.pvn.integration.platform.api.v1.models.StreamEnableRequest
import ru.pvn.integration.platform.api.v1.models.StreamEnableResponse
import ru.pvn.integration.platform.api.v1.models.StreamReadRequest
import ru.pvn.integration.platform.api.v1.models.StreamReadResponse
import ru.pvn.integration.platform.api.v1.models.StreamSearchRequest
import ru.pvn.integration.platform.api.v1.models.StreamSearchResponse
import ru.pvn.integration.platform.api.v1.models.StreamUpdateRequest
import ru.pvn.integration.platform.api.v1.models.StreamUpdateResponse
import ru.pvn.learning.IPContext
import ru.pvn.learning.helpers.makeIPError
import ru.pvn.learning.models.IPState.*
import ru.pvn.learning.models.IPWorkMode
import toTransport


suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
  appSettings: ApplicationSettings,
  logger: Logger,
) {
  val context = IPContext(
    workMode = when (appSettings.mode) {
      Mode.PROD -> IPWorkMode.PROD
      Mode.STUB -> IPWorkMode.STUB
      Mode.TEST -> IPWorkMode.TEST
    }
  )

  try {
    logger.info("Request started")
    context.fromTransport(receive<Q>())
    val processor = appSettings.ipStreamProcessor
    processor.exec(context)
    logger.info("Request processed")
    respond(context.toTransport() as R)

  } catch (e: Throwable) {
    logger.info("Request failed")
    context.state = FAILING
    context.errors.add(e.makeIPError())
    respond(context.toTransport() as R)

  }
}

val LOGG_CREATE = LoggerFactory.getLogger("process.v1.create")
val LOG_READ = LoggerFactory.getLogger("process.v1.read")
val LOG_UPDATE = LoggerFactory.getLogger("process.v1.update")
val LOG_DELETE = LoggerFactory.getLogger("process.v1.delete")
val LOG_SEARCH = LoggerFactory.getLogger("process.v1.search")
val LOG_DISABLE = LoggerFactory.getLogger("process.v1.disable")
val LOG_ENABLE = LoggerFactory.getLogger("process.v1.enable")
val LOG_ACCESIBLE = LoggerFactory.getLogger("process.v1.accesible")

suspend fun ApplicationCall.streamCreate(appSettings: ApplicationSettings) =
  processV1<StreamCreateRequest, StreamCreateResponse>(appSettings, LOGG_CREATE)

suspend fun ApplicationCall.streamRead(appSettings: ApplicationSettings) =
  processV1<StreamReadRequest, StreamReadResponse>(appSettings, LOG_READ)

suspend fun ApplicationCall.streamUpdate(appSettings: ApplicationSettings) =
  processV1<StreamUpdateRequest, StreamUpdateResponse>(appSettings, LOG_UPDATE)

suspend fun ApplicationCall.streamDelete(appSettings: ApplicationSettings) =
  processV1<StreamDeleteRequest, StreamDeleteResponse>(appSettings, LOG_DELETE)

suspend fun ApplicationCall.streamSearch(appSettings: ApplicationSettings) =
  processV1<StreamSearchRequest, StreamSearchResponse>(appSettings, LOG_SEARCH)

suspend fun ApplicationCall.streamDisable(appSettings: ApplicationSettings) =
  processV1<StreamDisableRequest, StreamDisableResponse>(appSettings, LOG_DISABLE)

suspend fun ApplicationCall.streamEnable(appSettings: ApplicationSettings) =
  processV1<StreamEnableRequest, StreamEnableResponse>(appSettings, LOG_ENABLE)

suspend fun ApplicationCall.streamAccessible(appSettings: ApplicationSettings) =
  processV1<StreamAccessibleRequest, StreamAccessibleResponse>(appSettings, LOG_ACCESIBLE)

