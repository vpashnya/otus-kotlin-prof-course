package ru.pvn.learning.models

data class IPStreamFilter(
  var searchString: String = "",
  var classShortName: String = "",
  var methodShortName: String = "",
  var active: Boolean = false,
)
