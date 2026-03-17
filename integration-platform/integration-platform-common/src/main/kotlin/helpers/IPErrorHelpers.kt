package ru.pvn.learning.helpers

import ru.pvn.learning.models.IPError

fun Throwable.makeIPError(
  code: String = "unknown",
  group: String = "exceptions",
  message: String = this.message ?: "",
) = IPError(
  code = code,
  group = group,
  message = message,
  exception = this
)