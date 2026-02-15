plugins {
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.pvn.integration.platform"
version = "0.0.1"


allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  group = rootProject.group
  version = rootProject.version
}

ext {
  val specDir = layout.projectDirectory.dir("../specs")
  set("spec", specDir.file("specs-integration-platform.yaml").toString())
}

tasks {
  register("build" ) {
    group = "build"
  }
  register("check" ) {
    group = "verification"
    subprojects.forEach { proj ->
      println("PROJ $proj")
      proj.getTasksByName("check", false).also {
        this@register.dependsOn(it)
      }
    }
  }
}