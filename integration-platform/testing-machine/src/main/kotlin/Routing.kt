package ru.pvn.learning.testing.machine

import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
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

    get("disableAllRestStreams") {
      val respTest = systemWithRestIntegration.disableAllStreams()
      call.respond(respTest)
    }

    get("sendSyntheticDataForRestStreams") {
      val respTest = systemWithKafkaIntegration.sendSyntheticDataForStreams()
      call.respond(respTest)
    }


  }
}