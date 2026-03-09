package ru.pvn.integration.platform.kafka.v1

import apiV1RequestDeserialize
import apiV1ResponseSerialize
import fromTransport
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.pvn.integration.platform.ApplicationSettings
import ru.pvn.integration.platform.Mode
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
    logger.info("Message deserialize...")
    val request = apiV1RequestDeserialize<IRequest>(request)
    context.fromTransport(request)
    logger.info("Message deserialized")

    logger.info("Message process")
    applicationSettings.ipStreamProcessor.exec(context)
    logger.info("Message processed")

  } catch (e: Throwable) {
    logger.error("Failed ${e.message}")
    context.state = FAILING
    context.errors.add(e.makeIPError())

  }

  logger.info("Return response")
  val response = apiV1ResponseSerialize(context.toTransport())
  logger.debug("response : $response")

  return response
}