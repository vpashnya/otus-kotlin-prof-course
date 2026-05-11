package ru.pvn.ancient

import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond

import io.ktor.server.routing.*

fun Application.configureRouting() {
  routing {
    route("magicgate") {
      post("tooktook") {
        call.respond(""" <<<${call.receive<String>()}>>> be in ancient monolith, congratulation!!!""")
      }
    }
  }
}