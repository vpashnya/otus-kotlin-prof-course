plugins {
  kotlin("jvm") version "2.1.21"
}

group = "ru.pvn.learning"
version = "unspecified"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(21)
}