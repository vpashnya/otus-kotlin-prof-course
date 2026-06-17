rootProject.name = "ancient-monolith"

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

pluginManagement {
  plugins {
    id("build-jvm") apply false
  }
}

