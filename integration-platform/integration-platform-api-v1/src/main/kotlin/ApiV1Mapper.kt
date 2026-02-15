import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.IResponse

val apiV1Mapper = JsonMapper.builder().run {
  enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL).
  build()
}

@Suppress("unused")
fun apiV1RequestSerialize(request: IRequest): String = apiV1Mapper.writeValueAsString(request)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
  apiV1Mapper.readValue(json, IRequest::class.java) as T

@Suppress("unused")
fun apiV1ResponseSerialize(response: IResponse): String = apiV1Mapper.writeValueAsString(response)

@Suppress("UNCHECKED_CAST", "unused")
fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
  apiV1Mapper.readValue(json, IResponse::class.java) as T