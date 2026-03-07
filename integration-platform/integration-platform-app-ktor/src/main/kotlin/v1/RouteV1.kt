package ru.pvn.integration.v1

import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1IP() {
  route("ip") {
    post("stream/create") { call.streamCreate() }
    post("stream/read") { call.streamRead() }
    post("stream/search") { call.streamSearch() }
    post("stream/update") { call.streamUpdate() }
    post("stream/delete") { call.streamDelete() }
    post("stream/enable") { call.streamEnable() }
    post("stream/disable") { call.streamDisable() }
    post("stream/accessible") { call.streamAccessible() }
  }
}