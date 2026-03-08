package ru.pvn.integration.v1

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ru.pvn.integration.ApplicationSettings

fun Route.v1IP(appSettings: ApplicationSettings) {
  route("ip/stream") {
    post("create") { call.streamCreate(appSettings) }
    post("read") { call.streamRead(appSettings) }
    post("search") { call.streamSearch(appSettings) }
    post("update") { call.streamUpdate(appSettings) }
    post("delete") { call.streamDelete(appSettings) }
    post("enable") { call.streamEnable(appSettings) }
    post("disable") { call.streamDisable(appSettings) }
    post("accessible") { call.streamAccessible(appSettings) }
  }
}