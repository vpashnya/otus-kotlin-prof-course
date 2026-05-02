package ru.pvn.learning

import DBIPStream
import DBIPStream.Companion.all
import DBIPStreams.active
import DBIPStreams.classShortName
import DBIPStreams.description
import DBIPStreams.methodShortName
import DBIPStreams.transportParams
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.statements.UpdateStatement
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
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
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import java.sql.DriverManager

class RepoStreamInPg : IRepoStream {
  constructor(credentials: PgCredentials) {
    runMigration(credentials)
    exposedConnect(credentials)
  }

  private fun runMigration(credentials: PgCredentials) {
    val connection = credentials.run { DriverManager.getConnection(url, user, password) }
    val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
    val liquibase = Liquibase("db/changelog/ip_stream.yaml", ClassLoaderResourceAccessor(), database)
    liquibase.update("")
  }

  private fun exposedConnect(credentials: PgCredentials) {
    credentials.let { creds ->
      val config = HikariConfig().apply {
        jdbcUrl = creds.url
        driverClassName = "org.postgresql.Driver"
        username = creds.user
        password = creds.password
        maximumPoolSize = creds.maximumPoolSize
        minimumIdle = creds.minimumIdle
        idleTimeout = creds.idleTimeout
        connectionTimeout = creds.connectionTimeout
      }

      val connectionPull = HikariDataSource(config)

      Database.connect(connectionPull)
    }
  }

  override suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    transaction {
      val ipStream = request.stream
      val dbIPStream = DBIPStream.new {
        description = ipStream.description
        classShortName = ipStream.classShortName
        methodShortName = ipStream.methodShortName
        transportParams = ipStream.transportParams
        version = 1
      }
      RepoIPStreamResponseOk(dbIPStream.mapToIPStream())
    }

  override suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse =
    transaction {
      try {
        buildRepoIPStreamResponseOk(request.streamId.asLong())
      } catch (e: Exception) {
        e.buildRepoIPStreamResponseError("002", "reading error")
      }
    }

  override suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    changeInDb(request, "003", "updating") { ipStream ->
      this[description] = ipStream.description
      this[classShortName] = ipStream.classShortName
      this[methodShortName] = ipStream.methodShortName
      this[transportParams] = ipStream.transportParams
      this[active] = false
    }

  override suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse =
    transaction {
      try {
        DBIPStream.find { DBIPStreams.id eq request.streamId.asLong() }.asSequence().firstOrNull()?.delete()?.let {
          RepoIPStreamResponseDeleteOk(request.streamId)
        } ?: buildRepoIPStreamResponseNotFoundError()
      } catch (e: Exception) {
        e.buildRepoIPStreamResponseError("003", "deleting error")
      }
    }

  override suspend fun enableStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    changeInDb(request, "004", "enabling") {
      this[active] = true
    }

  override suspend fun disableStream(request: RepoIPStreamRequest): RepoIPStreamResponse =
    changeInDb(request, "005", "disabling") {
      this[active] = false
    }

  override suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse =
    transaction {
      RepoIPStreamsResponseOk(
        streams =
          DBIPStream.find {
            (description like "%${request.description ?: ""}%") and
                (classShortName like "%${request.classNameLike ?: ""}%") and
                (methodShortName like "%${request.methodNameLike ?: ""}%")
          }.map { it.mapToIPStream() }.sortedBy { it.id.asLong() }
      )
    }

  override suspend fun accessibleStream() =
    transaction {
      RepoIPStreamsResponseOk(
        streams = all().asSequence().map { it.mapToIPStream() }.toList().sortedBy { it.id.asLong() }
      )
    }

  fun changeInDb(
    request: RepoIPStreamRequest,
    errorCode: String,
    action: String,
    func: UpdateStatement.(IPStream) -> Unit,
  ): RepoIPStreamResponse =
    transaction {
      try {
        val ipStream = request.stream
        val updatedRows = DBIPStreams.update(
          { (DBIPStreams.id eq ipStream.id.asLong()) and (DBIPStreams.version eq ipStream.version.asLong()) }
        ) {
          it.func(ipStream)
          it[version] = ipStream.version.asLong() + 1
        }
        if (updatedRows == 0) {
          buildRepoIPStreamResponseLockError(errorCode, "$action error")
        } else {
          buildRepoIPStreamResponseOk(ipStream.id.asLong())
        }
      } catch (e: Exception) {
        e.buildRepoIPStreamResponseError(errorCode, "$action error")
      }
    }

  private fun buildRepoIPStreamResponseNotFoundError() =
    RepoIPStreamResponseError(
      errors = listOf(
        IPError(code = "555", group = "DB", exception = null, message = "not found")
      )
    )

  private fun buildRepoIPStreamResponseOk(id: Long) =
    DBIPStream.find { DBIPStreams.id eq id }.asSequence().firstOrNull()?.mapToIPStream()
      ?.let { RepoIPStreamResponseOk(it) } ?: buildRepoIPStreamResponseNotFoundError()

  private fun Exception.buildRepoIPStreamResponseError(code: String, message: String) =
    RepoIPStreamResponseError(
      errors = listOf(
        IPError(code = code, group = "DB", exception = this, message = message)
      )
    )

  private fun buildRepoIPStreamResponseLockError(code: String, message: String) =
    RepoIPStreamResponseError(
      errors = listOf(
        IPError(code = "${code}999", group = "DB", exception = null, message = "$message , please retry")
      )
    )

  private fun DBIPStream.mapToIPStream() = IPStream(
    id = IPStreamId(id.value.toString()),
    description = description,
    classShortName = classShortName,
    methodShortName = methodShortName,
    transportParams = transportParams,
    active = active,
    version = IPStreamVersion(version.toString()),
  )
}
