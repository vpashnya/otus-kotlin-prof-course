package ru.pvn.learning.models

@JvmInline
value class IPStreamVersion(private val version: String) {
  fun asString() = version
  fun asLong() = version.toLong()

  companion object {
    val NONE = IPStreamVersion("0")
  }
}