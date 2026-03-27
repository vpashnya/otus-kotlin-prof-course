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
  testImplementation(kotlin("test"))
}
