package ru.pvn.integration.platform.ktor

data class ApplicationConfig(
  val mode: String,
)

fun getApplicationConfig() =
  ApplicationConfig(
    mode = getRequiredEnv("MODE"),
  )

fun getRequiredEnv(name: String): String {
  return System.getenv(name) ?: throw IllegalArgumentException("Missing required environment variable: $name")
}