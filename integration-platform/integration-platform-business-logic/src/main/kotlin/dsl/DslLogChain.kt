package ru.pvn.integration.platform.business.dsl

import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext

val LOG_CHAIN = LoggerFactory.getLogger("chain.exec")

@CorDslMarker
fun DslPerformerChain<IPContext>.log(level: Level, message: IPContext.() -> String) =
  performer {
    mainF {
      LOG_CHAIN.atLevel(level).log(message())
    }
  }


