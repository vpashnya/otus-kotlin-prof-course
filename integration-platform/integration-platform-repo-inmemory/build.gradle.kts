plugins {
  id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
  implementation(project(":integration-platform-common"))
  implementation(libs.ignite.core)
  implementation(libs.ignite.api)
  implementation(libs.ignite.runner)

  implementation(libs.logback.classic)
  implementation(libs.coroutines.test)

  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
  testImplementation(libs.kotlin.test.junit)
}

tasks.test {
  useJUnitPlatform()
  jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.lang.invoke=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.io=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.nio=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.math=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.time=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
  systemProperty("io.netty.tryReflectionSetAccessible", "true")
}

tasks.withType<JavaExec> {
  jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.lang.invoke=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.lang.reflect=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.io=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.nio=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.math=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/java.time=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/jdk.internal.access=ALL-UNNAMED")
  jvmArgs("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")
  systemProperty("io.netty.tryReflectionSetAccessible", "true")
}
