package ru.pvn.learning.processor.kafka.processor.metadata

data class IPStreamRecord(
  val classShortName: String,
  val methodShortName: String,
  val transportParams: String,
) {
  fun topicName() = "${transportParams}.${classShortName}.${methodShortName}"
  fun topicIn() = "${topicName()}.in"
  fun topicOut() = "${topicName()}.out"
}