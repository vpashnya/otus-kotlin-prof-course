plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-api-v1"))
  implementation(project(":integration-platform-api-v1-mappers"))
  implementation(libs.kafka.client)
  implementation(libs.logback.classic)
  implementation(libs.coroutines.core)
  implementation(libs.ktor.serialization.json)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.client.cio)


  implementation(kotlin("test-junit"))

  implementation("org.apache.kafka:kafka-clients:3.9.1")
  implementation("org.slf4j:slf4j-reload4j:2.0.17")
  implementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
  implementation("org.junit.platform:junit-platform-launcher:1.10.2")
  runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}



tasks.test {
  useJUnitPlatform()
}
