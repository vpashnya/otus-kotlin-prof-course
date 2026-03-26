import ru.pvn.integration.platform.business.dsl.stub
import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPWorkMode
import kotlin.test.Test
import kotlin.test.assertEquals

class DslBusinessTests {

  @Test
  fun dslCommandPositiveTest() {
    val context = IPContext(command = IPCommand.NONE)
    createChain {
      command(IPCommand.NONE) {
        performer {
          mainF { state = IPState.FINISHING }
        }
      }
    }.exec(context)

    assertEquals(context.state, IPState.FINISHING)
  }

  @Test
  fun dslCommandNegativeTest() {
    val context = IPContext(command = IPCommand.NONE)
    createChain {
      command(IPCommand.CREATE) {
        performer {
          mainF { state = IPState.FINISHING }
        }
      }
    }.exec(context)

    assertEquals(context.state, IPState.NONE)
  }

  @Test
  fun dslStubPositiveTest() {
    val context = IPContext(command = IPCommand.NONE, workMode = IPWorkMode.STUB)

    createChain {
      command(IPCommand.NONE) {
        stub {
          state = IPState.FINISHING
        }
      }
    }.exec(context)

    assertEquals(context.state, IPState.FINISHING)
  }

  @Test
  fun dslStubNegativeTest() {
    val context = IPContext(command = IPCommand.CREATE, workMode = IPWorkMode.TEST)
    createChain {
      command(IPCommand.CREATE) {
        stub {
          state = IPState.FINISHING
        }
      }
    }.exec(context)
    assertEquals(context.state, IPState.NONE)
  }


}