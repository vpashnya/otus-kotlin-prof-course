package ru.pvn.integration.platform.business.dsl

import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPWorkMode

@CorDslMarker
fun DslPerformerChain<IPContext>.stub(func: IPContext.() -> Unit) =
  performer {
    conditionF { workMode == IPWorkMode.STUB }
    mainF { func() }
  }
