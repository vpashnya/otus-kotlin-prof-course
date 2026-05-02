package ru.pvn.integration.platform.ktor

data class ApplicationConfig(
  val mode: String,
  val pgUrl: String,
  val pgUser: String,
  val pgPassword: String,
  val pgMaximumPoolSize: String,
  val pgMinimumIdle: String,
  val pgIdleTimeout: String,
  val pgConnectionTimeout: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = getRequiredEnv("MODE"),
    pgUrl = getRequiredEnv("PG_URL"),
    pgUser = getRequiredEnv("PG_USER"),
    pgPassword = getRequiredEnv("PG_PASSWORD"),
    pgMaximumPoolSize = getRequiredEnv("PG_MAXIMUM_POOL_SIZE"),
    pgMinimumIdle = getRequiredEnv("PG_MINIMUM_IDLE"),
    pgIdleTimeout = getRequiredEnv("PG_IDLE_TIMEOUT"),
    pgConnectionTimeout = getRequiredEnv("PG_CONNECTION_TIMEOUT"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}