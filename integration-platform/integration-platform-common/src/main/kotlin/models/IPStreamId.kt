package ru.pvn.learning.models

import kotlin.jvm.JvmInline

@JvmInline
value class IPStreamId(private val id: String) {
  fun asString() = id

  companion object {
    val NONE = IPStreamId("")
  }
}