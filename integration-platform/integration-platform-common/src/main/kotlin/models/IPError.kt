package ru.pvn.learning.models

data class IPError(
  val code: String = "",
  val group: String = "",
  val message: String = "",
  val exception: Throwable? = null,
)