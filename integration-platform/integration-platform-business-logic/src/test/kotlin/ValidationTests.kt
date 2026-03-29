import kotlinx.coroutines.test.runTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.business.dsl.validation
import ru.pvn.integration.platform.business.validation.validateClassNameFilled
import ru.pvn.integration.platform.business.validation.validateMethodDescriptionFilled
import ru.pvn.integration.platform.business.validation.validateMethodNameFilled
import ru.pvn.integration.platform.business.validation.validateNecessaryFieldsFilled
import ru.pvn.integration.platform.business.validation.validateStreamIdContainOnlyDigits
import ru.pvn.integration.platform.business.validation.validateStreamIdFilled
import ru.pvn.integration.platform.business.validation.validateTransportParamsFilled
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState.RUNNING
import ru.pvn.learning.models.IPState.FAILING
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamFilter
import ru.pvn.learning.models.IPStreamId
import kotlin.test.assertEquals

class ValidationTests {

  enum class ValidateCases(
    val contextOriginal: IPContext,
    val contextExpect: IPContext,
    val validateFunction: DslPerformerChain<IPContext>.() -> Unit,
  ) {
    VALIDATE_CLASS_NAME_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(classShortName = "  class_short_name   ")
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(classShortName = "  class_short_name   "),
        streamRequestValidating = IPStream(classShortName = "CLASS_SHORT_NAME"),
        streamRequestValidated = IPStream(classShortName = "CLASS_SHORT_NAME")
      ),
      validateFunction = DslPerformerChain<IPContext>::validateClassNameFilled
    ),
    VALIDATE_CLASS_NAME_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(classShortName = " ")
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(classShortName = " "),
        streamRequestValidating = IPStream(classShortName = ""),
        streamRequestValidated = IPStream(classShortName = ""),
        errors = mutableListOf(
          IPError(
            code = "V001",
            group = "validation",
            message = "Не заполнено название класса для интеграционного потока"
          )
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateClassNameFilled,
    ),
    VALIDATE_METHOD_NAME_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(methodShortName = "  method_short_name   ")
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(methodShortName = "  method_short_name   "),
        streamRequestValidating = IPStream(methodShortName = "METHOD_SHORT_NAME"),
        streamRequestValidated = IPStream(methodShortName = "METHOD_SHORT_NAME")
      ),
      validateFunction = DslPerformerChain<IPContext>::validateMethodNameFilled
    ),
    VALIDATE_METHOD_NAME_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(methodShortName = " ")
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(methodShortName = " "),
        streamRequestValidating = IPStream(methodShortName = ""),
        streamRequestValidated = IPStream(methodShortName = ""),
        errors = mutableListOf(
          IPError(
            code = "V002",
            group = "validation",
            message = "Не заполнено название метода для интеграционного потока"
          )
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateMethodNameFilled,
    ),
    VALIDATE_DESCRIPTION_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(description = "  some description   ")
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(description = "  some description   "),
        streamRequestValidating = IPStream(description = "some description"),
        streamRequestValidated = IPStream(description = "some description")
      ),
      validateFunction = DslPerformerChain<IPContext>::validateMethodDescriptionFilled
    ),
    VALIDATE_DESCRIPTION_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(description = " ")
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(description = " "),
        streamRequestValidating = IPStream(description = ""),
        streamRequestValidated = IPStream(description = ""),
        errors = mutableListOf(
          IPError(code = "V003", group = "validation", message = "Не заполнено описание для интеграционного потока")
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateMethodDescriptionFilled,
    ),
    VALIDATE_TRANSPORT_PARAMS_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(transportParams = """ {"param" : "value"} """)
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(transportParams = """ {"param" : "value"} """),
        streamRequestValidating = IPStream(transportParams = """{"param" : "value"}"""),
        streamRequestValidated = IPStream(transportParams = """{"param" : "value"}""")
      ),
      validateFunction = DslPerformerChain<IPContext>::validateTransportParamsFilled
    ),
    VALIDATE_TRANSPORT_PARAMS_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(transportParams = " ")
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(transportParams = " "),
        streamRequestValidating = IPStream(transportParams = ""),
        streamRequestValidated = IPStream(transportParams = ""),
        errors = mutableListOf(
          IPError(code = "V004", group = "validation", message = "Не заполнены транспортные параметры")
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateTransportParamsFilled,
    ),
    VALIDATE_STREAM_ID_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamRequestValidating = IPStream(id = IPStreamId("999")),
        streamRequestValidated = IPStream(id = IPStreamId("999")),
      ),
      validateFunction = DslPerformerChain<IPContext>::validateStreamIdFilled
    ),
    VALIDATE_STREAM_ID_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId.NONE)
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(id = IPStreamId.NONE),
        streamRequestValidating = IPStream(id = IPStreamId.NONE),
        streamRequestValidated = IPStream(id = IPStreamId.NONE),
        errors = mutableListOf(
          IPError(code = "V005", group = "validation", message = "Не заполнен id интеграционного потока")
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateStreamIdFilled,
    ),
    VALIDATE_STREAM_ID_CONTAINS_ONLY_DIGITS_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId("999"))
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId("999")),
        streamRequestValidating = IPStream(id = IPStreamId("999")),
        streamRequestValidated = IPStream(id = IPStreamId("999")),
      ),
      validateFunction = DslPerformerChain<IPContext>::validateStreamIdContainOnlyDigits
    ),
    VALIDATE_STREAM_ID_CONTAINS_ONLY_DIGITS_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamRequest = IPStream(id = IPStreamId("AAA"))
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamRequest = IPStream(id = IPStreamId("AAA")),
        streamRequestValidating = IPStream(id = IPStreamId("AAA")),
        streamRequestValidated = IPStream(id = IPStreamId("AAA")),
        errors = mutableListOf(
          IPError(code = "V006", group = "validation", message = "Id неподходящий формат")
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateStreamIdContainOnlyDigits,
    ),
    VALIDATE_NECESSARY_FIELDS_FILLED_SUCCESS(
      contextOriginal = IPContext(
        state = RUNNING,
        streamFilterRequest = IPStreamFilter(searchString = "methodName like '%NEW#AUTO%'", classShortName = "   KRED_CORP   ", methodShortName = "   NEW#AUTO   ", active = true)
      ),
      contextExpect = IPContext(
        state = RUNNING,
        streamFilterRequest = IPStreamFilter(searchString = "methodName like '%NEW#AUTO%'", classShortName = "   KRED_CORP   ", methodShortName = "   NEW#AUTO   ", active = true),
        streamFilterRequestValidating = IPStreamFilter(searchString = "methodName like '%NEW#AUTO%'", classShortName = "KRED_CORP", methodShortName = "NEW#AUTO", active = true),
        streamFilterRequestValidated = IPStreamFilter(searchString = "methodName like '%NEW#AUTO%'", classShortName = "KRED_CORP", methodShortName = "NEW#AUTO", active = true),
      ),
      validateFunction = DslPerformerChain<IPContext>::validateNecessaryFieldsFilled
    ),
    VALIDATE_NECESSARY_FIELDS_FILLED_FAIL(
      contextOriginal = IPContext(
        state = RUNNING,
        streamFilterRequest = IPStreamFilter()
      ),
      contextExpect = IPContext(
        state = FAILING,
        streamFilterRequest = IPStreamFilter(),
        streamFilterRequestValidating = IPStreamFilter(),
        streamFilterRequestValidated = IPStreamFilter(),
        errors = mutableListOf(
          IPError(code = "V011", group = "validation", message = "Не заполнено ни одно поле необходимое для поиска интеграционных потоков")
        )
      ),
      validateFunction = DslPerformerChain<IPContext>::validateNecessaryFieldsFilled
    )
  }

  @ParameterizedTest
  @EnumSource
  fun validateTest(validateCase: ValidateCases) = runTest {
    createChain {
      validation("Валидация") {
        validateCase.validateFunction.also { it() }
      }
    }.exec(validateCase.contextOriginal)

    assertEquals(validateCase.contextExpect, validateCase.contextOriginal)
  }
}
