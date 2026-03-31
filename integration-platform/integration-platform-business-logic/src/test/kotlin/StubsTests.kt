import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.business.dsl.stubs
import ru.pvn.integration.platform.business.stubs.stubCreateBadDescription
import ru.pvn.integration.platform.business.stubs.stubCreateSuccess
import ru.pvn.integration.platform.business.stubs.stubDeleteBadId
import ru.pvn.integration.platform.business.stubs.stubDeleteCannot
import ru.pvn.integration.platform.business.stubs.stubDeleteSuccess
import ru.pvn.integration.platform.business.stubs.stubDisableBadId
import ru.pvn.integration.platform.business.stubs.stubDisableSuccess
import ru.pvn.integration.platform.business.stubs.stubEnableBadId
import ru.pvn.integration.platform.business.stubs.stubEnableSuccess
import ru.pvn.integration.platform.business.stubs.stubReadBadId
import ru.pvn.integration.platform.business.stubs.stubReadNotFound
import ru.pvn.integration.platform.business.stubs.stubReadSuccess
import ru.pvn.integration.platform.business.stubs.stubSearchBadString
import ru.pvn.integration.platform.business.stubs.stubSearchSuccess
import ru.pvn.integration.platform.business.stubs.stubUpdateBadDescription
import ru.pvn.integration.platform.business.stubs.stubUpdateSuccess
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPWorkMode
import ru.pvn.learning.stubs.IPStubs
import kotlin.test.assertEquals

class StubsTests {

  enum class StubsCases(
    val contextOriginal: IPContext,
    val contextExpect: IPContext,
    val stubFunction: DslPerformerChain<IPContext>.() -> Unit,
  ) {
    CREATE_BAD_DESCRIPTION(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.CREATE,
        stubCase = IPStubs.BAD_DESCRIPTION
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.CREATE,
        stubCase = IPStubs.BAD_DESCRIPTION,
        errors = mutableListOf(IPError(code = "999", group = "CREATE_ERROR", message = "неверное описание"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubCreateBadDescription
    ),
    CREATE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.CREATE,
        stubCase = IPStubs.SUCCESS
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.CREATE,
        stubCase = IPStubs.SUCCESS,
        streamResponse = IPStream(id = IPStreamId("999"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubCreateSuccess
    ),
    UPDATE_BAD_DESCRIPTION(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.UPDATE,
        stubCase = IPStubs.BAD_DESCRIPTION,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.UPDATE,
        stubCase = IPStubs.BAD_DESCRIPTION,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "UPDATE_ERROR", message = "неверное описание"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubUpdateBadDescription
    ),
    UPDATE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.UPDATE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.UPDATE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamResponse = IPStream(id = IPStreamId("999")),
      ),
      stubFunction = DslPerformerChain<IPContext>::stubUpdateSuccess
    ),
    DELETE_BAD_ID(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.DELETE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.DELETE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubDeleteBadId
    ),
    CANNOT_DELETE(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.DELETE,
        stubCase = IPStubs.CANNOT_DELETE,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.DELETE,
        stubCase = IPStubs.CANNOT_DELETE,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubDeleteCannot
    ),
    DELETE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.DELETE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.DELETE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamResponse = IPStream(
          id = IPStreamId("999"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false,
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubDeleteSuccess
    ),
    READ_NOT_FOUND(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.READ,
        stubCase = IPStubs.NOT_FOUND,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.READ,
        stubCase = IPStubs.NOT_FOUND,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubReadNotFound
    ),
    READ_BAD_ID(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.READ,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.READ,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubReadBadId
    ),
    READ_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.READ,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.READ,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamResponse = IPStream(
          id = IPStreamId("999"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false,
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubReadSuccess
    ),
    ENABLE_BAD_ID(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.ENABLE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.ENABLE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "ENABLE_ERROR", message = "ошибка включения"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubEnableBadId
    ),
    ENABLE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.ENABLE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.ENABLE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamResponse = IPStream(
          id = IPStreamId("999"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = true,
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubEnableSuccess
    ),
    DISABLE_BAD_ID(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.DISABLE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.DISABLE,
        stubCase = IPStubs.BAD_ID,
        streamRequest = IPStream(id = IPStreamId("999")),
        errors = mutableListOf(IPError(code = "999", group = "DISABLE_ERROR", message = "ошибка отключения"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubDisableBadId
    ),
    DISABLE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.DISABLE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.DISABLE,
        stubCase = IPStubs.SUCCESS,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamResponse = IPStream(
          id = IPStreamId("999"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false,
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubDisableSuccess
    ),
    SEARCH_BAD_STRING(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.SEARCH,
        stubCase = IPStubs.BAD_SEARCH_STRING,
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.FAILING,
        command = IPCommand.SEARCH,
        stubCase = IPStubs.BAD_SEARCH_STRING,
        errors = mutableListOf(IPError(code = "999", group = "SEARCH_ERROR", message = "ошибка поиска"))
      ),
      stubFunction = DslPerformerChain<IPContext>::stubSearchBadString
    ),
    SEARCH_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.SEARCH,
        stubCase = IPStubs.SUCCESS,
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.SEARCH,
        stubCase = IPStubs.SUCCESS,
        streamsResponse = mutableListOf(
          IPStream(
            id = IPStreamId("1"),
            description = "Какое-то описание",
            classShortName = "SOME_CLASS",
            methodShortName = "SOME_METHOD",
            transportParams = "[1, 2, 3]",
            active = false,
          ),
          IPStream(
            id = IPStreamId("2"),
            description = "Какое-то описание",
            classShortName = "SOME_CLASS",
            methodShortName = "SOME_METHOD",
            transportParams = "[1, 2, 3]",
            active = false,
          ),
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubSearchSuccess
    ),
    ACCESSIBLE_SUCCESS(
      contextOriginal = IPContext(
        workMode = IPWorkMode.STUB,
        command = IPCommand.ACCESSIBLE,
        stubCase = IPStubs.SUCCESS,
      ),
      contextExpect = IPContext(
        workMode = IPWorkMode.STUB,
        state = IPState.RUNNING,
        command = IPCommand.ACCESSIBLE,
        stubCase = IPStubs.SUCCESS,
        streamsResponse = mutableListOf(
          IPStream(
            id = IPStreamId("1"),
            description = "Какое-то описание",
            classShortName = "SOME_CLASS",
            methodShortName = "SOME_METHOD",
            transportParams = "[1, 2, 3]",
            active = false,
          ),
          IPStream(
            id = IPStreamId("2"),
            description = "Какое-то описание",
            classShortName = "SOME_CLASS",
            methodShortName = "SOME_METHOD",
            transportParams = "[1, 2, 3]",
            active = false,
          ),
        )
      ),
      stubFunction = DslPerformerChain<IPContext>::stubSearchSuccess
    ),
  }

  @ParameterizedTest
  @EnumSource
  fun stubTest(stubCase: StubsCases) = runTest {
    createChain {
      initState("Инициализация состояния")
      command(stubCase.contextOriginal.command, "") {
        stubs("Заглушки") {
          stubCase.stubFunction.also { it() }
        }
      }
    }.exec(stubCase.contextOriginal)

    assertEquals(stubCase.contextExpect, stubCase.contextOriginal)
  }

}