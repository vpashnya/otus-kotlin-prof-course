package ru.pvn.learning.models

import ru.pvn.learning.models.IPStreamVersion

data class IPStream(
  var id: IPStreamId = IPStreamId.NONE,
  var description: String = "",
  var classShortName: String = "",
  var methodShortName: String = "",
  var transportParams: String = "",
  var active: Boolean = false,
  var version: IPStreamVersion = IPStreamVersion.NONE,
) {
  companion object {
    private val NONE = IPStream()
  }

  fun isEmpty() = this == NONE
}



