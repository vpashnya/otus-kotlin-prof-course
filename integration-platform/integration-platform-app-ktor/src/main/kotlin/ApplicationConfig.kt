package ru.pvn.integration.platform.ktor

data class ApplicationConfig(
  val mode: String,
  val pgUrl: String,
  val pgUser: String,
  val pgPassword: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = getRequiredEnv("MODE"),
    pgUrl = getRequiredEnv("PG_URL"),
    pgUser = getRequiredEnv("PG_USER"),
    pgPassword = getRequiredEnv("PG_PASSWORD"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}