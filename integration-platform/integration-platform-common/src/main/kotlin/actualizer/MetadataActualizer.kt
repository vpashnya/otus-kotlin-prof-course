package ru.pvn.learning.actualizer

interface MetadataActualizer {
  fun sendRefresh()

  companion object {
    val NONE = object : MetadataActualizer {
      override fun sendRefresh() { }
    }
  }
}
