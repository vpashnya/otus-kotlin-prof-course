import com.fasterxml.jackson.annotation.JsonProperty
import org.hamcrest.MatcherAssert.assertThat
import org.intellij.lang.annotations.Language
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamDebug
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugMode
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugStubs
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTests {
  private val testPair =
        StreamCreateRequest(
          debug = StreamDebug(mode = StreamRequestDebugMode.STUB, stub = StreamRequestDebugStubs.SUCCESS),
          stream = StreamCreateObject(
            classShortName = "CLIENT",
            methodShortName = "EXPORT2FNS",
            transportParams = "some transport",
            description = "Отправка информации в ФНС"
          )
        ) to """{"requestType":"create","requestType":null,"debug":{"mode":"stub","stub":"success"},"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС"}}"""

  @Test
  fun serialize() {
    val (s, f) = testPair
    assertEquals(
      apiV1Mapper.writeValueAsString(s), f
    )


  }

//
//  @Test
//  fun deserialize() {
//    val json = apiV1Mapper.writeValueAsString(request)
//    val obj = apiV1Mapper.readValue(json, IRequest::class.java) as AdCreateRequest
//
//    assertEquals(request, obj)
//  }

//  @Test
//  fun deserializeNaked() {
//    val jsonString = """
//            {"ad": null}
//        """.trimIndent()
//    val obj = apiV1Mapper.readValue(jsonString, AdCreateRequest::class.java)
//
//    assertEquals(null, obj.ad)
//  }

}