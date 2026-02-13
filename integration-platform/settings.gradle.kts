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
  }
}

include("integration-service")
include("integration-module")
include("integration-platform-api-v1")