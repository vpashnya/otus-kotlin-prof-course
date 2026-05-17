plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-repo-tests"))
  implementation(libs.logback.classic)
  implementation(libs.postgresql.lib)
  implementation(libs.liquibase.lib)
  implementation(libs.exposed.core)
  implementation(libs.exposed.jdbc)
  implementation(libs.exposed.dao)
  implementation(libs.hikari.cp)
  implementation(libs.testcontainers.postgres)

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.kotlin.test.junit)

}

tasks.test {
  useJUnitPlatform()
}
