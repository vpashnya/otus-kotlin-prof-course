plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(project(":integration-platform-lib-cor"))
  implementation(libs.logback.classic)
  implementation(libs.coroutines.test)

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.ktor.server.test.host)
  testImplementation(libs.kotlin.test.junit)
  testImplementation(libs.mockito)

}

tasks.test {
  useJUnitPlatform()
}
