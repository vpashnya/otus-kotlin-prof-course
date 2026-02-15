package ru.pvn.learning.models

@JvmInline
value class IPRequestId(private val id: String) {
  fun asString() = id

  companion object {
    val NONE = IPRequestId("")
  }
}