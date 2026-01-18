package kt2java

fun String.someExtension(arg: String): String {
  return "$this.someExtension($arg)"
}