package ru.pvn.integration.platform.business.dsl

import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand

@CorDslMarker
fun DslPerformerChain<IPContext>.command(ipCommand: IPCommand, func: DslPerformerChain<IPContext>.() -> Unit) =
  performers {
    conditionF { command == ipCommand }
    func()
  }