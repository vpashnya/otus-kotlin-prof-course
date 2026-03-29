package ru.pvn.learning.models

data class IPStream(
  var id: IPStreamId = IPStreamId.NONE,
  var description: String = "",
  var classShortName: String = "",
  var methodShortName: String = "",
  var transportParams: String = "",
  var active: Boolean = false,
) {
  companion object {
    private val NONE = IPStream()
  }

  fun isEmpty() = this == NONE
}



