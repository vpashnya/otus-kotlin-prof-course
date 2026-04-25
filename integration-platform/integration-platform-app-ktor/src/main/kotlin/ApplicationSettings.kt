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

  val pgCredentials = PgCredentials(
    url = applicationConfig.pgUrl,
    user = applicationConfig.pgUser,
    password = applicationConfig.pgPassword
  )

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
