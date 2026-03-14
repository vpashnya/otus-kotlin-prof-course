package ru.pvn.integration.platform.kafka.v1

import apiV1RequestDeserialize
import apiV1ResponseSerialize
import fromTransport
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.kafka.ApplicationSettings
import ru.pvn.integration.platform.kafka.Mode
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.learning.IPContext
import ru.pvn.learning.helpers.makeIPError
import ru.pvn.learning.models.IPState.FAILING
import ru.pvn.learning.models.IPWorkMode
import toTransport

val LOG = LoggerFactory.getLogger("process.v1")

suspend fun processV1(
  applicationSettings: ApplicationSettings,
  request: String,
  logger: Logger = LOG,
): String {
  val context = IPContext(
    workMode = when (applicationSettings.mode) {
      Mode.PROD -> IPWorkMode.PROD
      Mode.STUB -> IPWorkMode.STUB
      Mode.TEST -> IPWorkMode.TEST
    }
  )

  try {
    logger.info("Request started")
    val request = apiV1RequestDeserialize<IRequest>(request)
    context.fromTransport(request)

    logger.info("Request in processing...")
    applicationSettings.ipStreamProcessor.exec(context)
    logger.info("Request processed")

  } catch (e: Throwable) {
    logger.error("Request failed ${e.message}")
    context.state = FAILING
    context.errors.add(e.makeIPError())

  }

  val response = apiV1ResponseSerialize(context.toTransport())

  return response
}