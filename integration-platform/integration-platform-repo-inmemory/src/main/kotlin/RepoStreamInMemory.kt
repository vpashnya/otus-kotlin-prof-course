package ru.pvn.integration.platform.repo.inmemory

import app.cash.sqldelight.db.OptimisticLockException
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ru.pvn.integration.platform.`in`.memory.repo.InMemoryDatabase
import ru.pvn.integration.platform.`in`.memory.repo.InMemoryDatabaseQueries
import ru.pvn.integration.platform.`in`.memory.repo.Ip_stream
import ru.pvn.integration.platform.`in`.memory.repo.Ip_stream.Version
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamRequest
import ru.pvn.learning.repo.RepoIPStreamResponse
import ru.pvn.learning.repo.RepoIPStreamResponseDeleteOk
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponse
import ru.pvn.learning.repo.RepoIPStreamsResponseFail
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import java.util.concurrent.atomic.AtomicInteger

class RepoStreamInMemory(
  private val seqId: AtomicInteger = AtomicInteger(0),
  private val db: InMemoryDatabase = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).let { driver ->
    InMemoryDatabase.Schema.create(driver)
    InMemoryDatabase(driver)
  },
) : IRepoStream {

  override suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
    try {
      request.apply {
        val result =
          db.inMemoryDatabaseQueries.insertIpStream(
            id = seqId.incrementAndGet().toLong(),
            description = stream.description,
            classShortName = stream.classShortName,
            methodShortName = stream.methodShortName,
            transportParams = stream.transportParams,
          )
        return RepoIPStreamResponseOk(
          stream = stream.copy(id = IPStreamId(result.value.toString()), active = false)
        )
      }
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "001", group = "DB", message = "inserting error", exception = e,
          )
        )
      )
    }
  }

  override suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
    try {
      val ipStream =
        db.inMemoryDatabaseQueries.selectIPStream(request.streamId.asString().toLong()).executeAsOne().mapToIpStream()
      return RepoIPStreamResponseOk(ipStream)
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "002", group = "DB", message = "reading error", exception = e,
          )
        )
      )
    }
  }

  override suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
    try {
      request.run {
        return withLock("updating", stream.id) { version ->
          updateIPStream(
            description = stream.description,
            classShortName = stream.classShortName,
            methodShortName = stream.methodShortName,
            transportParams = stream.transportParams,
            active = false,
            version = version,
            id = stream.id.asString().toLong(),
          )
        }
      }
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "004", group = "DB", message = "updating error, please retry", exception = e,
          )
        )
      )
    }
  }

  override suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
    try {
      db.inMemoryDatabaseQueries.deleteIPStream(request.streamId.asString().toLong())
      return RepoIPStreamResponseDeleteOk(request.streamId)
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "005", group = "DB", message = "deleting error, please retry", exception = e,
          )
        )
      )
    }
  }

  override suspend fun enableStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
    try {
      request.apply {
        return withLock("enabling", streamId) { version ->
          enableIPStream(version, streamId.asString().toLong())
        }
      }
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "007", group = "DB", message = "enabling error, please retry", exception = e,
          )
        )
      )
    }
  }

  override suspend fun disableStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
    try {
      request.apply {
        return withLock("disabling", streamId) { version ->
          disableIPStream(version, streamId.asString().toLong())
        }
      }
    } catch (e: Exception) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "009", group = "DB", message = "disabling error, please retry", exception = e,
          )
        )
      )
    }
  }

  override suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse {
    try {
      return RepoIPStreamsResponseOk(
        streams = db.inMemoryDatabaseQueries.selectLikeIPStreams(
          classShortName = request.classNameLike ?: "%",
          methodShortName = request.methodNameLike ?: "%",
          active = request.active.toString()
        ).executeAsList().map { it.mapToIpStream() }
      )
    } catch (e: Exception) {
      return RepoIPStreamsResponseFail(
        errors = listOf(
          IPError(
            code = "009", group = "DB", message = "searching error, please retry", exception = e,
          )
        )
      )
    }
  }

  override suspend fun accessibleStream(): RepoIPStreamsResponse {
    try {
      return RepoIPStreamsResponseOk(
        streams = db.inMemoryDatabaseQueries.selectAllIpStreams().executeAsList().map { it.mapToIpStream() }
      )
    } catch (e: Exception) {
      return RepoIPStreamsResponseFail(
        errors = listOf(
          IPError(
            code = "009", group = "DB", message = "searching error, please retry", exception = e,
          )
        )
      )
    }
  }

  suspend fun withLock(
    action: String,
    ipStreamId: IPStreamId,
    dbUpdate: InMemoryDatabaseQueries.(version: Version?) -> Unit,
  ): RepoIPStreamResponse {
    try {
      db.inMemoryDatabaseQueries.apply {
        val id = ipStreamId.asString().toLong()
        val version = getIPStreamVersion(id).executeAsOne().version
        dbUpdate(version)
        return RepoIPStreamResponseOk(stream = selectIPStream(id).executeAsOne().mapToIpStream())
      }
    } catch (e: OptimisticLockException) {
      return RepoIPStreamResponseError(
        errors = listOf(
          IPError(
            code = "099", group = "DB", message = "$action error, record is lock, please retry", exception = e,
          )
        )
      )
    }
  }

  fun Ip_stream.mapToIpStream() =
    IPStream(
      id = IPStreamId(id.toString()),
      description = description ?: "",
      classShortName = classShortName ?: "",
      methodShortName = methodShortName ?: "",
      transportParams = transportParams ?: "",
      active = active ?: false,
    )
}