plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ktor)
  alias(libs.plugins.kotlin.plugin.serialization)
  alias(libs.plugins.sqldelight.pl)
}

group = rootProject.group
version = rootProject.version

application {
  mainClass = "io.ktor.server.tomcat.jakarta.EngineMain"
}

dependencies {
  implementation(libs.ktor.serialization.jackson)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.server.core)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.server.tomcat.jakarta)
  implementation(libs.logback.classic)
  implementation(libs.ktor.server.config.yaml)
  implementation(libs.ktor.serialization.json)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.negotiation)
  implementation(libs.postgresql.lib)
  implementation(libs.liquibase.lib)
  implementation(libs.exposed.core)
  implementation(libs.exposed.jdbc)
  implementation(libs.exposed.dao)
  implementation(libs.sqldelight.driver)
  implementation(libs.hikari.cp)

  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-api-v1"))
  implementation(project(":integration-platform-api-v1-mappers"))
  implementation(project(":integration-platform-business-logic"))
  implementation(project(":integration-platform-repo-inmemory"))
  implementation(project(":integration-platform-repo-pg"))

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.ktor.server.test.host)
  testImplementation(libs.kotlin.test.junit)
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<Jar> {
  manifest {
    attributes["Main-Class"] = "ru.pvn.integration.platform.ktor.ApplicationKt"
  }

  duplicatesStrategy = DuplicatesStrategy.EXCLUDE

  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}
