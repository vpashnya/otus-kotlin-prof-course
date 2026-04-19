import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.business.dsl.repoProcess
import ru.pvn.integration.platform.business.repo.accessibleInRepo
import ru.pvn.integration.platform.business.repo.createInRepo
import ru.pvn.integration.platform.business.repo.deleteInRepo
import ru.pvn.integration.platform.business.repo.disableInRepo
import ru.pvn.integration.platform.business.repo.enableInRepo
import ru.pvn.integration.platform.business.repo.readInRepo
import ru.pvn.integration.platform.business.repo.searchInRepo
import ru.pvn.integration.platform.business.repo.updateInRepo
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPWorkMode
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamResponseDeleteOk
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamsResponseError
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import kotlin.test.assertEquals

class RepoProcessTests {
  enum class SingleInRepoProcessCases(
    val repoProcessFunction: DslPerformerChain<IPContext>.() -> Unit,
    val resultSuccess: Boolean = true,
    val toRepo: IPStream = IPStream(id = IPStreamId("1"), description = "before"),
    val fromRepo: IPStream = IPStream(id = IPStreamId("1"), description = "after"),
    val manyFromRepo: MutableList<IPStream> = mutableListOf(),
  ) {
    CREATE_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::createInRepo
    ),
    CREATE_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::createInRepo,
      resultSuccess = false
    ),
    READ_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::readInRepo
    ),
    READ_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::readInRepo,
      resultSuccess = false
    ),
    UPDATE_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::updateInRepo
    ),
    UPDATE_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::updateInRepo,
      resultSuccess = false
    ),
    DISABLE_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::disableInRepo
    ),
    DISABLE_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::disableInRepo,
      resultSuccess = false
    ),
    ENABLE_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::enableInRepo
    ),
    ENABLE_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::enableInRepo,
      resultSuccess = false
    ),
    DELETE_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::deleteInRepo,
      fromRepo = IPStream(id = IPStreamId("1"))
    ),
    DELETE_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::deleteInRepo,
      resultSuccess = false
    ),
    SEARCH_SUCCESS(
      repoProcessFunction = DslPerformerChain<IPContext>::searchInRepo,
      toRepo = IPStream(),
      fromRepo = IPStream(),
      manyFromRepo = mutableListOf(IPStream(id = IPStreamId("1")))
    ),
    SEARCH_FAIL(
      repoProcessFunction = DslPerformerChain<IPContext>::searchInRepo,
      resultSuccess = false
    ),
    ACCESSIBLE(
      repoProcessFunction = DslPerformerChain<IPContext>::accessibleInRepo,
      toRepo = IPStream(),
      fromRepo = IPStream(),
      manyFromRepo = mutableListOf(IPStream(id = IPStreamId("1")))
    );
  }

  @ParameterizedTest
  @EnumSource
  fun singleInRepoTest(processRepoCase: SingleInRepoProcessCases) = runTest {
    val toRepo = processRepoCase.toRepo
    val fromRepo = processRepoCase.fromRepo
    val manyFromRepo = processRepoCase.manyFromRepo
    val errorFromRepo = mutableListOf(IPError(code = "999"))
    val repo = mock<IRepoStream>()

    if (processRepoCase.resultSuccess) {
      whenever(repo.createStream(any())).thenReturn(RepoIPStreamResponseOk(fromRepo))
      whenever(repo.readStream(any())).thenReturn(RepoIPStreamResponseOk(fromRepo))
      whenever(repo.updateStream(any())).thenReturn(RepoIPStreamResponseOk(fromRepo))
      whenever(repo.enableStream(any())).thenReturn(RepoIPStreamResponseOk(fromRepo))
      whenever(repo.disableStream(any())).thenReturn(RepoIPStreamResponseOk(fromRepo))
      whenever(repo.deleteStream(any())).thenReturn(RepoIPStreamResponseDeleteOk(fromRepo.id))
      whenever(repo.searchStreams(any())).thenReturn(RepoIPStreamsResponseOk(manyFromRepo))
      whenever(repo.accessibleStream()).thenReturn(RepoIPStreamsResponseOk(manyFromRepo))
    } else {
      whenever(repo.createStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.readStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.updateStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.enableStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.disableStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.deleteStream(any())).thenReturn(RepoIPStreamResponseError(errorFromRepo))
      whenever(repo.searchStreams(any())).thenReturn(RepoIPStreamsResponseError(errorFromRepo))
    }

    val context =
      IPContext(workMode = IPWorkMode.TEST, state = IPState.RUNNING, streamRepo = repo, streamRequest = toRepo)

    createChain {
      initState("Инициализация состояния")
      command(IPCommand.NONE, "") {
        repoProcess("Работа с репозиторием") {
          processRepoCase.repoProcessFunction.also { it() }
        }
      }
    }.exec(context)

    if (processRepoCase.resultSuccess) {
      assertEquals(fromRepo, context.streamResponseFromRepo)
      assertEquals(manyFromRepo, context.streamsResponseFromRepo)
    } else {
      assertEquals(errorFromRepo, context.errors)
    }
  }
}
