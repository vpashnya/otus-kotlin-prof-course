import ru.pvn.integration.platform.business.stubs.execStubs
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPWorkMode

class IPStreamProcessor {
  suspend fun exec(context: IPContext) {
    if (context.workMode == IPWorkMode.STUB) {
      execStubs(context)
    }
  }
}