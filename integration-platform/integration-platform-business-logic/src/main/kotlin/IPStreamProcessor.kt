import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.business.dsl.stubs
import ru.pvn.integration.platform.business.dsl.validation
import ru.pvn.integration.platform.business.stubs.*
import ru.pvn.integration.platform.business.validation.validateClassNameFilled
import ru.pvn.integration.platform.business.validation.validateMethodDescriptionFilled
import ru.pvn.integration.platform.business.validation.validateMethodNameFilled
import ru.pvn.integration.platform.business.validation.validateNecessaryFieldsFilled
import ru.pvn.integration.platform.business.validation.validateStreamId
import ru.pvn.integration.platform.business.validation.validateTransportParamsFilled
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand.*

class IPStreamProcessor {
  suspend fun exec(context: IPContext) =
    createChain {
      initState("Инициализация состояния")

      command(CREATE, "Создание интеграционного потока") {
        stubs("Обработка заглушек") {
          stubCreateBadDescription()
          stubCreateSuccess()
        }
        validation("Валидация") {
          validateClassNameFilled()
          validateMethodNameFilled()
          validateMethodDescriptionFilled()
          validateTransportParamsFilled()
        }
      }
      command(UPDATE, "Изменение интеграционного потока") {
        stubs("Обработка заглушек") {
          stubUpdateBadDescription()
          stubUpdateSuccess()
        }
        validation("Валидация") {
          validateClassNameFilled()
          validateMethodNameFilled()
          validateMethodDescriptionFilled()
          validateTransportParamsFilled()
        }
      }
      command(DELETE, "Удаление интеграционного потока") {
        stubs("Обработка заглушек") {
          stubDeleteBadId()
          stubDeleteCannot()
          stubDeleteSuccess()
        }
        validation("Валидация") {
          validateStreamId()
        }
      }
      command(READ, "Чтение информации об интеграционном потоке") {
        stubs(("Обработка заглушек")) {
          stubReadNotFound()
          stubReadBadId()
          stubReadSuccess()
        }
        validation("Валидация") {
          validateStreamId()
        }
      }
      command(ENABLE, "Включение интеграционного потока") {
        stubs(("Обработка заглушек")) {
          stubEnableBadId()
          stubEnableSuccess()
        }
        validation("Валидация") {
          validateStreamId()
        }

      }
      command(DISABLE, "Отключение интеграционного потока") {
        stubs("Обработка заглушек") {
          stubDisableBadId()
          stubDisableSuccess()
        }
        validation("Валидация") {
          validateStreamId()
        }
      }
      command(SEARCH, "Поиск интеграционных потоков") {
        stubs("Обработка заглушек") {
          stubSearchBadString()
          stubSearchSuccess()
        }
        validation("Валидация") {
          validateNecessaryFieldsFilled()
        }
      }
      command(ACCESSIBLE, "Доступные интеграционные потоки") {
        stubs("Обработка заглушек") {
          stubAccessibleSuccess()
        }
      }
    }.exec(context)
}


