package ru.pvn.ktor.processor.processor.metadata

data class IPStreamRecord(
  val classShortName: String,
  val methodShortName: String,
  val transportParams: String,
)

fun IPStreamRecord.lowercase() = this.copy(
  classShortName = classShortName.lowercase(),
  methodShortName = methodShortName.lowercase(),
  transportParams = transportParams.lowercase()
)