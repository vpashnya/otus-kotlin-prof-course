package ru.pvn.integration.platform.business.dsl

import org.slf4j.event.Level.WARN
import org.slf4j.event.Level.INFO
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPState.RUNNING
import ru.pvn.learning.models.IPWorkMode.PROD
import ru.pvn.learning.models.IPWorkMode.TEST

@CorDslMarker
fun DslPerformerChain<IPContext>.repoProcess(title: String, func: DslPerformerChain<IPContext>.() -> Unit) =
  performers {
    conditionF { state == RUNNING && (workMode == PROD || workMode == TEST) }
    log(INFO) { "$title - начало" }

    performer {
      mainF {
        streamRequestToRepo = streamRequest.copy()
        streamFilterRequestToRepo = streamFilterRequest.copy()
      }
    }

    func()

    performer {
      conditionF { state == IPState.FAILING }
      mainF {
        log(WARN) { "Ошибка при работе с БД: ${errors.last()}" }
      }
    }

    log(INFO) { "$title - конец" }
  }