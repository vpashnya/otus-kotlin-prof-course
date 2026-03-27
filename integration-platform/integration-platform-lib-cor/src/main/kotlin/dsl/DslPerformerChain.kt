package ru.pvn.integration.platform.lib.cor.dsl

import ru.pvn.integration.platform.lib.cor.IPerformer
import ru.pvn.integration.platform.lib.cor.PerformerChain

@CorDslMarker
class DslPerformerChain<T> {
  private var conditionFunction: suspend T.() -> Boolean = { true }
  private val perfs: MutableList<IPerformer<T>> = mutableListOf()

  fun conditionF(func: suspend T.() -> Boolean) {
    conditionFunction = func
  }

  fun performer(func: DslPerformerSingle<T>.() -> Unit) =
    perfs.add(DslPerformerSingle<T>().performer(func))

  fun performers(func: DslPerformerChain<T>.() -> Unit) =
    DslPerformerChain<T>()
      .apply { func() }
      .build()
      .also { perfs.add(it) }

  fun build() = PerformerChain(conditionFunction, perfs)

}
