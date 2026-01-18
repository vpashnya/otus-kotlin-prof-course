pluginManagement {
  plugins {
    val kotlinVersion: String by settings
    kotlin("jvm") version kotlinVersion
  }
}

plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "otus-kotlin-prof-course"

includeBuild("lessons")
includeBuild("integration-platform")
includeBuild("build-plugin")
