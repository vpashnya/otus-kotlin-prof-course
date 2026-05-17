package ru.pvn.learning

data class PgCredentials(
  val url: String,
  val user: String,
  val password: String,
  val maximumPoolSize: Int,
  val minimumIdle: Int,
  val idleTimeout: Long,
  val connectionTimeout: Long,
)
