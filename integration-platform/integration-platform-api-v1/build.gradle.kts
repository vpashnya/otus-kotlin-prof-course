import org.gradle.kotlin.dsl.kotlin

plugins {
  id("build-jvm")
  alias(libs.plugins.openapi.generator)
}

sourceSets {
  main {
    java.srcDir(layout.buildDirectory.dir("generate-resources/main/src/main/kotlin"))
  }
}

openApiGenerate {
  val openapiGroup = "${rootProject.group}.api.v1"
  generatorName.set("kotlin")
  packageName.set(openapiGroup)
  apiPackage.set("$openapiGroup.api")
  modelPackage.set("$openapiGroup.models")
  invokerPackage.set("$openapiGroup.invoker")
  inputSpec.set(rootProject.ext["spec"] as String) // <-

  globalProperties.apply {
    put("models", "")
    put("modelDocs", "false")
  }

  /**
   * Настройка дополнительных параметров из документации по генератору
   * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
   */
  configOptions.set(
    mapOf(
      "dateLibrary" to "string",
      "enumPropertyNaming" to "UPPERCASE",
      "serializationLibrary" to "jackson",
      "collectionType" to "list"
    )
  )
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(libs.jackson.kotlin)
  implementation(libs.jackson.datatype)
  testRuntimeOnly(libs.jupiter.engine)
  testImplementation(libs.jupiter.params)
  testImplementation(libs.jupiter.api)
}

tasks {
  compileKotlin {
    dependsOn(openApiGenerate)
  }
}

tasks.test {
  useJUnitPlatform()
}