import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.business.dsl.repoProcess
import ru.pvn.integration.platform.business.dsl.stubs
import ru.pvn.integration.platform.business.dsl.validation
import ru.pvn.integration.platform.business.repo.accessibleInRepo
import ru.pvn.integration.platform.business.repo.createInRepo
import ru.pvn.integration.platform.business.repo.deleteInRepo
import ru.pvn.integration.platform.business.repo.disableInRepo
import ru.pvn.integration.platform.business.repo.enableInRepo
import ru.pvn.integration.platform.business.repo.readInRepo
import ru.pvn.integration.platform.business.repo.searchInRepo
import ru.pvn.integration.platform.business.repo.updateInRepo
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
        repoProcess("Сохранение нового объекта в БД"){
          createInRepo()
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
        repoProcess("Изменение объекта в БД"){
          updateInRepo()
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
        repoProcess("Удаление объекта в БД"){
          deleteInRepo()
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
        repoProcess("Получение объекта из БД"){
          readInRepo()
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
        repoProcess("Изменение объекта в БД"){
          enableInRepo()
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
        repoProcess("Изменение объекта в БД"){
          disableInRepo()
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
        repoProcess("Получение объектов из БД"){
          searchInRepo()
        }
      }
      command(ACCESSIBLE, "Доступные интеграционные потоки") {
        stubs("Обработка заглушек") {
          stubAccessibleSuccess()
        }
        repoProcess("Получение объектов из БД"){
          accessibleInRepo()
        }
      }
    }.exec(context)
}


