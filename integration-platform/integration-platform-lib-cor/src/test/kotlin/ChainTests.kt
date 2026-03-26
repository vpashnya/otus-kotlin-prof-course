import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.lib.cor.PerformerSingle
import kotlin.test.assertEquals

class ChainTests {

  enum class PerformerSingleCases(
    val conditionFunction: TestContext.() -> Boolean = { true },
    val mainFunction: TestContext.() -> Unit = { someField = "main value" },
    val exceptionFunction: TestContext.(Exception) -> Unit = { someField = "exception value" },
    val expect: String,
  ) {
    CONDITION_POSITIVE(
      expect = "main value"
    ),
    CONDITION_NEGATIVE(
      conditionFunction = { false },
      expect = "default value"
    ),
    EXCEPTION_THROW(
      mainFunction = { throw Exception() },
      expect = "exception value"
    )
  }

  @ParameterizedTest
  @EnumSource
  fun createPerformerSingleTest(case: PerformerSingleCases) {
    val context = TestContext()

    PerformerSingle<TestContext>(
      case.conditionFunction, case.mainFunction, case.exceptionFunction
    ).exec(context)

    assertEquals(context.someField, case.expect)
  }

  data class TestContext(var someField: String = "default value")
}
