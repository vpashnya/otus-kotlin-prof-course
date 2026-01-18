rootProject.name = "integration-platform"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

pluginManagement {
  includeBuild("../build-plugin")
  plugins {
    id("build-jvm") apply false
    id("build-kmp") apply false
  }
}

include("integration-service")
include("integration-service-kmp")
