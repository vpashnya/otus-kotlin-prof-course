package ru.pvn.integration.platform.lib.cor.dsl

import ru.pvn.integration.platform.lib.cor.PerformerSingle

@CorDslMarker
class DslPerformerSingle<T> {
  var conditionFunction: T.() -> Boolean = { true }
  var mainFunction: T.() -> Unit = {}
  var exceptionFunction: T.(Exception) -> Unit = {}

  fun conditionF(func: T.() -> Boolean) {
    conditionFunction = func
  }

  fun mainF(func: T.() -> Unit) {
    mainFunction = func
  }

  fun exceptionF(func: T.(Exception) -> Unit) {
    exceptionFunction = func
  }

  fun build() = PerformerSingle(conditionFunction, mainFunction, exceptionFunction)

  fun performer(func: DslPerformerSingle<T>.() -> Unit): PerformerSingle<T> {
    func()
    return build()
  }

}
