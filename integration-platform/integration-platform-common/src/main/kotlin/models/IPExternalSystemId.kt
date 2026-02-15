package ru.pvn.learning.models

@JvmInline
value class IPExternalSystemId(private val id: String) {
  fun asString() = id

  companion object {
    val NONE = IPExternalSystemId("")
  }
}