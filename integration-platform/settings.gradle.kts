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
include(":integration-platform-common")
include(":integration-platform-api-v1")
include(":integration-platform-api-v1-mappers")
include(":integration-platform-app-ktor")
include(":integration-platform-app-kafka")
include(":integration-platform-business-logic")
include(":integration-platform-lib-cor")
include(":integration-platform-repo-pg")
include(":integration-platform-repo-inmemory")
include(":integration-platform-repo-tests")
include(":metadata-actualizer")
include(":integration-processor-kafka")
include(":integration-processor-ktor")
include(":testing-machine")
