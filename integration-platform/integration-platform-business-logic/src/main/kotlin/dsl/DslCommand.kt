package ru.pvn.integration.platform.business.dsl

import org.slf4j.event.Level.*
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand

@CorDslMarker
fun DslPerformerChain<IPContext>.command(
  ipCommand: IPCommand,
  title: String,
  func: DslPerformerChain<IPContext>.() -> Unit,
) =
  performers {
    conditionF { command == ipCommand }
    log(INFO) { "command: $command - $title" }
    func()
  }