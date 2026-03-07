package ru.pvn.integration.v1

import fromTransport
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
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
import toTransport

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1() {
  val context = IPContext()
  try {
    println("create post begin")
    val request = receive<Q>()
    context.fromTransport(request)

    println("request :  $request")
    println("context :  $context")

    val response = context.toTransport() as R
    respond(response)

  } catch (e: Throwable) {
    context.state = FAILING
    context.errors.add(e.makeIPError())
    val response = context.toTransport() as R
    respond(response)
  }
}

suspend fun ApplicationCall.streamCreate() = processV1<StreamCreateRequest, StreamCreateResponse>()
suspend fun ApplicationCall.streamRead() = processV1<StreamReadRequest, StreamReadResponse>()
suspend fun ApplicationCall.streamUpdate() = processV1<StreamUpdateRequest, StreamUpdateResponse>()
suspend fun ApplicationCall.streamDelete() = processV1<StreamDeleteRequest, StreamDeleteResponse>()
suspend fun ApplicationCall.streamSearch() = processV1<StreamSearchRequest, StreamSearchResponse>()
suspend fun ApplicationCall.streamDisable() = processV1<StreamDisableRequest, StreamDisableResponse>()
suspend fun ApplicationCall.streamEnable() = processV1<StreamEnableRequest, StreamEnableResponse>()
suspend fun ApplicationCall.streamAccessible() = processV1<StreamAccessibleRequest, StreamAccessibleResponse>()

