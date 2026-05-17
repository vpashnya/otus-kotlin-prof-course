package ru.pvn.learning


import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory
import ru.pvn.learning.config.ApplicationConfig
import ru.pvn.learning.config.getApplicationConfig

lateinit var applicationConfig: ApplicationConfig

fun main() {
  applicationConfig = getApplicationConfig()

  val requests =
    LauncherDiscoveryRequestBuilder
      .request()
      .selectors(selectPackage("ru.pvn.learning.tests"))
      .build()

  val launcher = LauncherFactory.create()
  launcher.execute(requests)
}