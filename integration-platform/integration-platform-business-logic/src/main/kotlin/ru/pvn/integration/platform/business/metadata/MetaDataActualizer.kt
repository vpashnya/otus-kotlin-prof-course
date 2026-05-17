package ru.pvn.integration.platform.business.metadata

import ru.pvn.integration.platform.business.dsl.log
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import org.slf4j.event.Level.*

fun DslPerformerChain<IPContext>.metadataRefresh() =
  performer {
    mainF {
      metadataActualizer.sendRefresh()
      log(INFO) { "Отправлено событие обновления метаданных" }
    }
  }