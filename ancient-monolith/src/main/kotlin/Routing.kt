package ru.pvn.ancient

import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond

import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("ancient.monolith")

fun Application.configureRouting() {
  routing {
    route("magicgate") {
      post("tooktook") {
        val message = call.receive<String>()
        logger.info("received : $message")
        call.respond(""" <<<$message>>> be in ancient monolith, congratulation!!!""")
      }
    }
  }
}