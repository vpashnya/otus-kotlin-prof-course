package ru.pvn.integration.platform.lib.cor.dsl

import ru.pvn.integration.platform.lib.cor.PerformerChain

@CorDslMarker
fun <T> createChain(func: DslPerformerChain<T>.() -> Unit): PerformerChain<T> =
  DslPerformerChain<T>()
    .apply { func() }
    .build()
