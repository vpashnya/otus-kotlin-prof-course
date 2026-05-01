package ru.pvn.integration.platform.repo.inmemory

import app.cash.sqldelight.db.OptimisticLockException
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ru.pvn.integration.platform.`in`.memory.repo.InMemoryDatabase
import ru.pvn.integration.platform.`in`.memory.repo.Ip_stream
import ru.pvn.integration.platform.`in`.memory.repo.Ip_stream.Version
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPStreamVersion
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamRequest
import ru.pvn.learning.repo.RepoIPStreamResponse
import ru.pvn.learning.repo.RepoIPStreamResponseDeleteOk
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponse
import ru.pvn.learning.repo.RepoIPStreamsResponseError
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import java.util.concurrent.atomic.AtomicInteger

class RepoStreamInMemory(
  private val seqId: AtomicInteger = AtomicInteger(0),
  private val db: InMemoryDatabase = {
    Class.forName("org.sqlite.JDBC")
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).let { driver ->
      InMemoryDatabase.Schema.create(driver)
      InMemoryDatabase(driver)
    }
  }(),
) : IRepoStream {
  private val queries = db.inMemoryDatabaseQueries

  override suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    try {
      val id = seqId.incrementAndGet().toLong()
      request.stream.run {
        queries.insertIpStream(
          id = id,
          description = description,
          classShortName = classShortName,
          methodShortName = methodShortName,
          transportParams = transportParams,
        ).run { buildRepoIPStreamResponseOk(id) }
      }
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("001", "inserting error")
    }

  override suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse =
    try {
      buildRepoIPStreamResponseOk(request.streamId.asLong())
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("002", "reading error")
    }

  override suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    try {
      request.stream.run {
        queries.updateIPStream(
          description = description,
          classShortName = classShortName,
          methodShortName = methodShortName,
          transportParams = transportParams,
          active = false,
          version = Version(version.asLong()),
          id = id.asString().toLong(),
        )
        buildRepoIPStreamResponseOk(id.asLong())
      }
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("003", "updating error")
    }

  override suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse =
    try {
      val result = queries.deleteIPStream(request.streamId.asLong())
      if (result.value == 0L) {
        buildRepoIPStreamResponseNotFoundError()
      } else {
        RepoIPStreamResponseDeleteOk(request.streamId)
      }
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("003", "deleting error")
    }

  override suspend fun enableStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    try {
      request.stream.run {
        queries.enableIPStream(Version(version.asLong()), id.asLong())
        buildRepoIPStreamResponseOk(id.asLong())
      }
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("004", "enabling error")
    }

  override suspend fun disableStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    try {
      request.stream.run {
        queries.disableIPStream(Version(version.asLong()), id.asLong())
        buildRepoIPStreamResponseOk(id.asLong())
      }
    } catch (e: Exception) {
      e.buildRepoIPStreamResponseError("005", "disabling error")
    }

  override suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse =
    try {
      RepoIPStreamsResponseOk(
        streams = queries.selectLikeIPStreams(
          classShortName = request.classNameLike ?: "%",
          methodShortName = request.methodNameLike ?: "%",
          description = request.description ?: "%"
        ).executeAsList().map { it.mapToIpStream() }
      )
    } catch (e: Exception) {
      e.buildRepoIPStreamsResponseError("010", "searching error, please retry")
    }

  override suspend fun accessibleStream(): RepoIPStreamsResponse =
    try {
      RepoIPStreamsResponseOk(
        streams = queries.selectAllIpStreams().executeAsList().map { it.mapToIpStream() }
      )
    } catch (e: Exception) {
      e.buildRepoIPStreamsResponseError("010", "searching error, please retry")
    }

  private fun buildRepoIPStreamResponseOk(id: Long) =
    queries.selectIPStream(id).executeAsOneOrNull()?.mapToIpStream()?.let { RepoIPStreamResponseOk(it) }
      ?: buildRepoIPStreamResponseNotFoundError()

  private fun buildRepoIPStreamResponseNotFoundError() =
    RepoIPStreamResponseError(
      errors = listOf(
        IPError(
          code = "555",
          group = "DB",
          exception = null,
          message = "not found",
        )
      )
    )

private fun Exception.buildRepoIPStreamResponseError(code: String, message: String) =
    RepoIPStreamResponseError(
      errors = listOf(
        IPError(
          code = """${code}${if (this is OptimisticLockException) "999" else ""}""",
          group = "DB",
          exception = this,
          message = """${message}${if (this is OptimisticLockException) " , please retry" else ""}""",
        )
      )
    )

  private fun Exception.buildRepoIPStreamsResponseError(code: String, message: String) =
    RepoIPStreamsResponseError(
      errors = listOf(
        IPError(
          code = code,
          group = "DB",
          exception = this,
          message = """${message}${if (this is OptimisticLockException) " , please retry" else ""}""",
        )
      )
    )

  private fun Ip_stream.mapToIpStream() =
    IPStream(
      id = IPStreamId(id.toString()),
      description = description ?: "",
      classShortName = classShortName ?: "",
      methodShortName = methodShortName ?: "",
      transportParams = transportParams ?: "",
      active = active ?: false,
      version = IPStreamVersion(version?.version.toString())
    )
}