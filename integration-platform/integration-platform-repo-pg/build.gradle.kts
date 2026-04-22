plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-repo-tests"))
  implementation(libs.logback.classic)

  implementation("org.postgresql:postgresql:42.7.8")
  implementation("org.liquibase:liquibase-core:4.32.0")
  implementation("org.jetbrains.exposed:exposed-core:1.2.0")
  implementation("org.jetbrains.exposed:exposed-jdbc:1.2.0")
  implementation("org.jetbrains.exposed:exposed-dao:1.2.0")


  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.kotlin.test.junit)
}

tasks.test {
  useJUnitPlatform()
}
