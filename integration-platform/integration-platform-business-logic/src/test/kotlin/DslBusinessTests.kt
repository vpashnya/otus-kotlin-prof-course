
import kotlinx.coroutines.test.runTest
import ru.pvn.integration.platform.business.dsl.stubs
import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.initState
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand.*
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPState.RUNNING
import ru.pvn.learning.models.IPState.FINISHING
import ru.pvn.learning.models.IPWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals

class DslBusinessTests {

  @Test
  fun dslCommandPositiveTest() = runTest {
    val context = IPContext(command = NONE)
    createChain {
      command(NONE, "") {
        performer {
          mainF { state = FINISHING }
        }
      }
    }.exec(context)
    assertEquals(context.state, FINISHING)
  }

  @Test
  fun dslCommandNegativeTest() = runTest {
    val context = IPContext(command = NONE)
    createChain {
      command(CREATE, "") {
        performer {
          mainF { state = FINISHING }
        }
      }
    }.exec(context)
    assertEquals(context.state, IPState.NONE)
  }

  @Test
  fun dslInitStateTest() = runTest {
    val context = IPContext(state = IPState.NONE)

    createChain {
      initState("Инициализация состояния")
    }.exec(context)

    assertEquals(context.state, RUNNING)
  }

  @Test
  fun dslStubPositiveTest() = runTest {
    val context = IPContext(command = NONE, workMode = IPWorkMode.STUB, state = RUNNING)

    createChain {
      command(NONE, "Тестовая команда") {
        stubs("Тестовые заглушки") {
          performer {
            mainF { state = FINISHING }
          }
        }
      }
    }.exec(context)
    assertEquals(context.state, FINISHING)
  }

  @Test
  fun dslStubNegativeTest() = runTest {
    val context = IPContext(command = CREATE, workMode = IPWorkMode.TEST, state = RUNNING)
    createChain {
      command(CREATE, "Тестовая команда") {
        stubs("Тестовые заглушки") {
          performer {
            mainF { state = FINISHING }
          }
        }
      }
    }.exec(context)
    assertEquals(context.state, RUNNING)
  }

}