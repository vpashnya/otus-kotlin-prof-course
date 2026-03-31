import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.lib.cor.PerformerChain
import ru.pvn.integration.platform.lib.cor.PerformerSingle
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import kotlin.test.assertEquals

class ChainTests {

  enum class PerformerSingleCases(
    val conditionFunction: TestContext.() -> Boolean = { true },
    val mainFunction: TestContext.() -> Unit = { someValue = "main value" },
    val exceptionFunction: TestContext.(Exception) -> Unit = { someValue = "exception value" },
    val expect: String,
  ) {
    CONDITION_POSITIVE(
      expect = "main value"
    ),
    CONDITION_NEGATIVE(
      conditionFunction = { false },
      expect = "default value"
    ),
    EXCEPTION(
      mainFunction = { throw Exception() },
      expect = "exception value"
    )
  }

  @ParameterizedTest
  @EnumSource
  fun performerSingleTest(case: PerformerSingleCases) {
    val context = TestContext()

    PerformerSingle(
      case.conditionFunction, case.mainFunction, case.exceptionFunction
    ).exec(context)

    assertEquals(context.someValue, case.expect)
  }

  @Test
  fun performerChainTest() {
    val context = TestContext()

    PerformerChain<TestContext>(
      conditionFunction = { true },
      mutableListOf(
        PerformerSingle({ true }, { someValues.add("value first") }, {}),
        PerformerSingle({ false }, { someValues.add("value second") }, {}),
        PerformerSingle({ true }, { throw Exception("value third") }, { e -> someValues.add(e.message!!) })
      )
    ).exec(context)

    assertEquals(context.someValues, mutableListOf("value first", "value third"))
  }

  @Test
  fun dslChainTest() {
    val context = TestContext()

    val chain =
      createChain<TestContext> {
        performer {
          mainF { someValues.add("first value") }
        }
        performers {
          performer {
            mainF { someValues.add("second value from inner chain") }
          }
          performer {
            conditionF { false }
            mainF { someValues.add("skipped value from inner chain") }
          }
          performers {
            performer {
              mainF { someValues.add("third value from inner inner chain") }
            }
            performer {
              mainF { throw Exception("fourth value from inner inner chain") }
              exceptionF { e -> someValues.add(e.message!!) }
            }
          }
          performers {
            conditionF { false }
            performer {
              mainF { someValues.add("skipped inner inner chain by condition") }
            }
          }
        }
        performer {
          mainF { someValues.add("sixth value") }
        }
      }

    chain.exec(context)

    assertEquals(
      context.someValues,
      mutableListOf(
        "first value",
        "second value from inner chain",
        "third value from inner inner chain",
        "fourth value from inner inner chain",
        "sixth value"
      )
    )
  }

  data class TestContext(
    var someValue: String = "default value",
    val someValues: MutableList<String> = mutableListOf(),
  )
}
