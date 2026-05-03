package ru.pvn.learning


import org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.launcher.core.LauncherFactory

fun main() {
  val requests = LauncherDiscoveryRequestBuilder.request()
    .selectors(
      selectPackage("ru.pvn.learning.tests")
    )
    .build()

  val launcher = LauncherFactory.create()
  launcher.execute(requests)
}