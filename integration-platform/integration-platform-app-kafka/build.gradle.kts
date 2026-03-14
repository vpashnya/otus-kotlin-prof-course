plugins {
  alias(libs.plugins.kotlin.jvm)
}

group = rootProject.group
version = rootProject.version


dependencies {
  implementation(libs.kafka.client)
  implementation(libs.logback.classic)
  implementation(libs.coroutines.core)
  implementation(libs.kotlinx.atomicfu)
  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-api-v1"))
  implementation(project(":integration-platform-api-v1-mappers"))
  implementation(project(":integration-platform-business-logic"))

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.kotlin.test.junit)
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "ru.pvn.integration.platform.kafka.MainKt"
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
