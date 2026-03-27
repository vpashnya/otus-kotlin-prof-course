package ru.pvn.integration.platform.business.dsl

import org.slf4j.event.Level.INFO
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPState.RUNNING
import ru.pvn.learning.models.IPWorkMode

@CorDslMarker
fun DslPerformerChain<IPContext>.stubs(title: String, func: DslPerformerChain<IPContext>.() -> Unit) =
  performers {
    conditionF { state == RUNNING && workMode == IPWorkMode.STUB }
    log(INFO) { "stubCase: $stubCase - $title" }
    func()
  }
