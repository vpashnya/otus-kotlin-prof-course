plugins {
  kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(kotlin("stdlib"))
  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-api-v1"))
  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
}

tasks.test {
  useJUnitPlatform()
}