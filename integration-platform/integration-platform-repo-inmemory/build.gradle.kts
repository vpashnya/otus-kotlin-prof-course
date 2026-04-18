plugins {
  id("build-jvm")
  alias(libs.plugins.kotlinx.serialization)
  alias(libs.plugins.sqldelight.pl)
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(libs.logback.classic)
  implementation(libs.coroutines.test)
  implementation(libs.sqldelight.driver)

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.kotlin.test.junit)
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  sourceSets {
    all {
      languageSettings.optIn("kotlin.time.ExperimentalTime")
    }
  }
}

sqldelight {
  databases {
    register("InMemoryDatabase") {
      packageName.set("ru.pvn.integration.platform.in.memory.repo")
    }
  }
}