package ru.pvn.learning.models

data class IPStream(
  var id: IPStreamId = IPStreamId.NONE,
  var description: String = "",
  var classShortName: String = "",
  var methodShortName: String = "",
  var transportParams: String = "",
  var active: Boolean = false,
) {
  fun isEmpty() = this == NONE

  companion object {
    private val NONE = IPStream()
  }
}



