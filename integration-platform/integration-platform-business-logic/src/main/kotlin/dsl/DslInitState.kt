package ru.pvn.integration.platform.business.dsl

import org.slf4j.event.Level.INFO
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPState

@CorDslMarker
fun DslPerformerChain<IPContext>.initState(
  title: String,
) =
  performers {
    conditionF { state == IPState.NONE }
    log(INFO) { title }
    performer {
      mainF { state = IPState.RUNNING }
    }
    log(INFO) { "state: $state" }
  }