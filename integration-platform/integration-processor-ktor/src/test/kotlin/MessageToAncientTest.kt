import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import org.junit.Test
import ru.pvn.ktor.processor.MessageToAncient
import ru.pvn.ktor.processor.processor.metadata.IPStreamRecord
import kotlin.test.assertEquals

class MessageToAncientTest {
  val mapper = JsonMapper.builder().run {
    enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL).build()
  }

  @Test
  fun test() {
    val message = MessageToAncient(
      ipStream = IPStreamRecord(
        classShortName = "Test",
        methodShortName = "test",
        transportParams = "test",
      ),
      message = "message for test"
    )
    val expect = mapper.writeValueAsString(message)
    assertEquals(
      """{"ipStream":{"classShortName":"Test","methodShortName":"test","transportParams":"test"},"message":"message for test"}""",
      expect
    )


  }
}