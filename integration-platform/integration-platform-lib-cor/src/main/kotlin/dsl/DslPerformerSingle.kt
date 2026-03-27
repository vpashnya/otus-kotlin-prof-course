package ru.pvn.integration.platform.lib.cor.dsl

import ru.pvn.integration.platform.lib.cor.PerformerSingle

@CorDslMarker
class DslPerformerSingle<T> {
  var conditionFunction: suspend T.() -> Boolean = { true }
  var mainFunction: suspend T.() -> Unit = {}
  var exceptionFunction: suspend T.(Exception) -> Unit = {}

  fun conditionF(func: suspend T.() -> Boolean) {
    conditionFunction = func
  }

  fun mainF(func: suspend T.() -> Unit) {
    mainFunction = func
  }

  fun exceptionF(func: suspend T.(Exception) -> Unit) {
    exceptionFunction = func
  }

  fun build() = PerformerSingle(conditionFunction, mainFunction, exceptionFunction)

  fun performer(func: DslPerformerSingle<T>.() -> Unit): PerformerSingle<T> {
    func()
    return build()
  }

}
