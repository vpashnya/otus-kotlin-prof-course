plugins {
  kotlin("multiplatform")
}

repositories {
  mavenCentral()
}

kotlin {
  jvm {
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }

  js {
    browser {
      testTask {
        useKarma {
          // Выбираем браузеры, на которых будет тестироваться
          useChrome()
          useFirefox()
        }
        // Без этой настройки длительные тесты не отрабатывают
        useMocha {
          timeout = "100s"
        }
      }
    }
  }

  linuxX64()


  val coroutinesVersion: String by project
  val datetimeVersion: String by project

  // Description of modules corresponding to our target platforms
  //  common - common code that we can use on different platforms
  //  for each target platform, we can specify our own specific dependencies
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
      }
    }
    commonTest {
      dependencies {
        implementation(kotlin("test"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
      }
    }
    jvmMain {
    }
    jvmTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    nativeMain {
    }
  }
}