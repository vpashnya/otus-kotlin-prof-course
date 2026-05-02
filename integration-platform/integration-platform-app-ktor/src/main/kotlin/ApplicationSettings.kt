package ru.pvn.integration.platform.ktor

import IPStreamProcessor
import ru.pvn.integration.platform.repo.inmemory.RepoStreamInMemory
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.integration.platform.ktor.Mode.*
import ru.pvn.learning.PgCredentials
import ru.pvn.learning.RepoStreamInPg

data class ApplicationSettings(
  val mode: Mode,
  val ipStreamProcessor: IPStreamProcessor,
  val ipStreamRepo: IRepoStream = IRepoStream.NONE,
)

enum class Mode {
  PROD, TEST, STUB
}

fun initApplicationSettings(applicationConfig: ApplicationConfig): ApplicationSettings {
  val mode = Mode.valueOf(applicationConfig.mode)

  val pgCredentials = applicationConfig.run {
    PgCredentials(
      url = pgUrl,
      user = pgUser,
      password = pgPassword,
      maximumPoolSize = pgMaximumPoolSize.run { if (isNotEmpty()) toInt() else 0 },
      minimumIdle = pgMinimumIdle.run { if (isNotEmpty()) toInt() else 0 },
      idleTimeout = pgIdleTimeout.run { if (isNotEmpty()) toLong() else 0L },
      connectionTimeout = pgConnectionTimeout.run { if (isNotEmpty()) toLong() else 0L },
    )
  }

  return ApplicationSettings(
    mode = mode,
    ipStreamProcessor = IPStreamProcessor(),
    ipStreamRepo = when (mode) {
      PROD -> RepoStreamInPg(pgCredentials)
      TEST -> RepoStreamInMemory()
      STUB -> IRepoStream.NONE
    }
  )
}
