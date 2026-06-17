plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(libs.kafka.client)
  implementation(libs.logback.classic)
  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
}

tasks.test {
  useJUnitPlatform()
}
