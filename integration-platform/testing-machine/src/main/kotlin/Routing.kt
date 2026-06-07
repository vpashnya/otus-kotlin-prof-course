package ru.pvn.learning.testing.machine

import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import ru.pvn.learning.config.ApplicationConfigData
import ru.pvn.learning.config.getApplicationConfig
import ru.pvn.learning.tests.SystemWithKafkaIntegration
import ru.pvn.learning.tests.SystemWithRestIntegration


fun Application.configureRouting() {
  val applicationConfig = (getApplicationConfig() as ApplicationConfigData)
  val systemWithKafkaIntegration = SystemWithKafkaIntegration(applicationConfigData = applicationConfig)
  val systemWithRestIntegration = SystemWithRestIntegration(applicationConfigData = applicationConfig)

  routing {
    get("createRandomKafkaStreams") {
      val respTest = systemWithKafkaIntegration.sendFillingMetadataToKafka()
      call.respond(respTest)
    }

    get("enableRandomKafkaStreams") {
      val respTest = systemWithKafkaIntegration.enableRandomStreams()
      call.respond(respTest)
    }

    get("enableAllKafkaStreams") {
      val respTest = systemWithKafkaIntegration.enableAllStreams()
      call.respond(respTest)
    }

    get("disableAllKafkaStreams") {
      val respTest = systemWithKafkaIntegration.disableAllStreams()
      call.respond(respTest)
    }

    get("sendSyntheticDataForKafkaStreams") {
      val respTest = systemWithKafkaIntegration.sendSyntheticDataForStreams()
      call.respond(respTest)
    }

    get("createRandomRestStreams") {
      val respTest = systemWithRestIntegration.sendFillingMetadataToRest()
      call.respond(respTest)
    }

    get("enableRandomRestStreams") {
      val respTest = systemWithRestIntegration.enableRandomStreams()
      call.respond(respTest)
    }

    get("enableAllRestStreams") {
      val respTest = systemWithRestIntegration.enableAllStreams()
      call.respond(respTest)
    }

    get("disableAllRestStreams") {
      val respTest = systemWithRestIntegration.disableAllStreams()
      call.respond(respTest)
    }

    get("sendSyntheticDataForRestStreams") {
      val respTest = systemWithRestIntegration.sendSyntheticDataForStreams()
      call.respond(respTest)
    }

    get("sendCrashTest") {
      call.respond("Crash test running!")
      runBlocking {
        CoroutineScope(Dispatchers.Default).launch {
          systemWithKafkaIntegration.apply {
            repeat(1000) {
              sendSyntheticDataForStreams(false)
              delay(100)
            }
          }
        }
      }

      runBlocking {
        CoroutineScope(Dispatchers.Default).launch {
          systemWithRestIntegration.apply {
            repeat(1000) {
              sendSyntheticDataForStreams()
              delay(100)
            }
          }
        }
      }

    }
  }
}