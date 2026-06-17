plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-api-v1"))
  implementation(project(":integration-platform-api-v1-mappers"))
  implementation(libs.kafka.client)
  implementation(libs.coroutines.core)
  implementation(libs.ktor.serialization.jackson)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.server.core)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.server.tomcat.jakarta)
  implementation(libs.logback.classic)
  implementation(libs.ktor.server.config.yaml)
  implementation(libs.ktor.serialization.json)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.negotiation)

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.ktor.server.test.host)
  testImplementation(libs.kotlin.test.junit)


  implementation(kotlin("test-junit"))

  testRuntimeOnly(libs.jupiter.engine)
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "ru.pvn.learning.testing.machine.ApplicationKt"
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
