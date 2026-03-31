package ru.pvn.integration.platform.business.dsl

import org.slf4j.event.Level.WARN
import org.slf4j.event.Level.INFO
import ru.pvn.integration.platform.lib.cor.dsl.CorDslMarker
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState.RUNNING
import ru.pvn.learning.models.IPState.FAILING
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamFilter
import ru.pvn.learning.models.IPWorkMode.*


@CorDslMarker
fun DslPerformerChain<IPContext>.validation(title: String, func: DslPerformerChain<IPContext>.() -> Unit) =
  performers {
    conditionF { state == RUNNING && (workMode == PROD || workMode == TEST) }
    log(INFO) { "$title - начало" }

    performer {
      mainF {
        streamRequestValidating = streamRequest.copy()
        streamRequestValidating.classShortName = streamRequestValidating.classShortName.trim().uppercase()
        streamRequestValidating.methodShortName = streamRequestValidating.methodShortName.trim().uppercase()
        streamRequestValidating.description = streamRequestValidating.description.trim()
        streamRequestValidating.transportParams = streamRequestValidating.transportParams.trim()

        streamFilterRequestValidating = streamFilterRequest.copy()
        streamFilterRequestValidating.classShortName = streamFilterRequestValidating.classShortName.trim().uppercase()
        streamFilterRequestValidating.methodShortName = streamFilterRequestValidating.methodShortName.trim().uppercase()
        streamFilterRequestValidating.searchString = streamFilterRequestValidating.searchString.trim()
      }
    }

    func()

    performer {
      mainF {
        streamRequestValidated = streamRequestValidating.copy()
        streamFilterRequestValidated = streamFilterRequestValidating.copy()
      }
    }

    log(INFO) { "$title - конец" }
  }

@CorDslMarker
fun DslPerformerChain<IPContext>.validationStreamRequest(
  code: String,
  message: String,
  condition: IPStream.() -> Boolean,
) =
  performer {
    conditionF { streamRequestValidating.condition() }
    mainF {
      errors.add(IPError(code = code, group = "validation", message = message))
      state = FAILING
      log(WARN) { "Ошибка валидации интеграционного потока $message" }
    }
  }

@CorDslMarker
fun DslPerformerChain<IPContext>.validationStreamFilter(
  code: String,
  message: String,
  condition: IPStreamFilter.() -> Boolean,
) =
  performer {
    conditionF { streamFilterRequestValidating.condition() }
    mainF {
      errors.add(IPError(code = code, group = "validation", message = message))
      state = FAILING
      log(WARN) { "Ошибка валидации строки поиска $message" }
    }
  }
