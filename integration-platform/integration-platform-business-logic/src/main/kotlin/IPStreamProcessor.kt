
import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.business.dsl.stubs
import ru.pvn.integration.platform.business.stubs.*
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
      }
      command(UPDATE, "Изменение интеграционного потока") {
        stubs("Обработка заглушек") {
          stubUpdateBadDescription()
          stubUpdateSuccess()
        }
      }
      command(DELETE, "Удаление интеграционного потока") {
        stubs("Обработка заглушек") {
          stubDeleteBadId()
          stubDeleteCannot()
          stubDeleteSuccess()
        }
      }
      command(READ, "Чтение информации об интеграционном потоке") {
        stubs(("Обработка заглушек")) {
          stubReadNotFound()
          stubReadBadId()
          stubReadSuccess()
        }
      }
      command(ENABLE, "Включение интеграционного потока") {
        stubs(("Обработка заглушек")) {
          stubEnableBadId()
          stubEnableSuccess()
        }
      }
      command(DISABLE, "Отключение интеграционного потока") {
        stubs("Обработка заглушек") {
          stubDisableBadId()
          stubDisableSuccess()
        }
      }
      command(SEARCH, "Поиск интеграционных потоков") {
        stubs("Обработка заглушек") {
          stubSearchBadString()
          stubSearchSuccess()
        }
      }
      command(ACCESSIBLE, "Доступные интеграционные потоки") {
        stubs("Обработка заглушек") {
          stubAccessibleSuccess()
        }
      }
    }.exec(context)
}


