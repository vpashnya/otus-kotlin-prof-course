plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
  implementation(libs.plugin.kotlin)
  implementation(libs.plugin.binaryCompatibilityValidator)
}

gradlePlugin {
  plugins {
    register("build-jvm") {
      id = "build-jvm"
      implementationClass = "ru.pvn.plugin.BuildPluginJvm"
    }
    register("build-kmp") {
      id = "build-kmp"
      implementationClass = "ru.pvn.plugin.BuildPluginMultiplatform"
    }
  }
}

