package ru.pvn.integration.platform.lib.cor.dsl

import ru.pvn.integration.platform.lib.cor.IPerformer
import ru.pvn.integration.platform.lib.cor.PerformerChain

@CorDslMarker
class DslPerformerChain<T> {
  private val performers: MutableList<IPerformer<T>> = mutableListOf()

  fun performer(func: DslPerformerSingle<T>.() -> Unit) =
    performers.add(DslPerformerSingle<T>().performer(func))

  fun performers(func: DslPerformerChain<T>.() -> Unit) =
    performers.add(DslPerformerChain<T>().apply { func() }.build())

  fun build() = PerformerChain(performers = performers)

}
