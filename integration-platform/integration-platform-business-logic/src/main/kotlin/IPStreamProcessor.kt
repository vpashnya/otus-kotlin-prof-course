import ru.pvn.integration.platform.business.dsl.command
import ru.pvn.integration.platform.business.dsl.stub
import ru.pvn.integration.platform.business.stubs.createStubs
import ru.pvn.integration.platform.business.stubs.deleteStubs
import ru.pvn.integration.platform.business.stubs.disableStubs
import ru.pvn.integration.platform.business.stubs.enableStubs
import ru.pvn.integration.platform.business.stubs.readStubs
import ru.pvn.integration.platform.business.stubs.searchAccessible
import ru.pvn.integration.platform.business.stubs.searchStubs
import ru.pvn.integration.platform.business.stubs.updateStubs
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPCommand

class IPStreamProcessor {
  suspend fun exec(context: IPContext) =
    createChain {
      command(IPCommand.CREATE) {
        stub { createStubs() }
      }
      command(IPCommand.UPDATE) {
        stub { updateStubs() }
      }
      command(IPCommand.DELETE) {
        stub { deleteStubs() }
      }
      command(IPCommand.READ) {
        stub { readStubs() }
      }
      command(IPCommand.ENABLE) {
        stub { enableStubs() }
      }
      command(IPCommand.DISABLE) {
        stub { disableStubs() }
      }
      command(IPCommand.SEARCH) {
        stub { searchStubs() }
      }
      command(IPCommand.ACCESSIBLE) {
        stub { searchAccessible() }
      }
    }.exec(context)

}


