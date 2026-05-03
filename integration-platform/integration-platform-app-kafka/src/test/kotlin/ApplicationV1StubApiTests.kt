import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import ru.pvn.integration.platform.api.v1.models.Error
import ru.pvn.integration.platform.api.v1.models.IRequest
import ru.pvn.integration.platform.api.v1.models.IResponse
import ru.pvn.integration.platform.api.v1.models.ResponseResult
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleRequest
import ru.pvn.integration.platform.api.v1.models.StreamAccessibleResponse
import ru.pvn.integration.platform.api.v1.models.StreamCreateObject
import ru.pvn.integration.platform.api.v1.models.StreamCreateRequest
import ru.pvn.integration.platform.api.v1.models.StreamCreateResponse
import ru.pvn.integration.platform.api.v1.models.StreamDebug
import ru.pvn.integration.platform.api.v1.models.StreamDeleteRequest
import ru.pvn.integration.platform.api.v1.models.StreamDeleteResponse
import ru.pvn.integration.platform.api.v1.models.StreamDisableRequest
import ru.pvn.integration.platform.api.v1.models.StreamDisableResponse
import ru.pvn.integration.platform.api.v1.models.StreamEnableRequest
import ru.pvn.integration.platform.api.v1.models.StreamEnableResponse
import ru.pvn.integration.platform.api.v1.models.StreamReadRequest
import ru.pvn.integration.platform.api.v1.models.StreamReadResponse
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugMode
import ru.pvn.integration.platform.api.v1.models.StreamRequestDebugStubs
import ru.pvn.integration.platform.api.v1.models.StreamResponseObject
import ru.pvn.integration.platform.api.v1.models.StreamSearchFilter
import ru.pvn.integration.platform.api.v1.models.StreamSearchRequest
import ru.pvn.integration.platform.api.v1.models.StreamSearchResponse
import ru.pvn.integration.platform.api.v1.models.StreamUpdateObject
import ru.pvn.integration.platform.api.v1.models.StreamUpdateRequest
import ru.pvn.integration.platform.api.v1.models.StreamUpdateResponse
import ru.pvn.integration.platform.kafka.ApplicationConfigData
import ru.pvn.integration.platform.kafka.IPStreamHandler
import ru.pvn.integration.platform.kafka.initApplicationSettings
import java.util.Collections
import kotlin.String
import kotlin.test.assertEquals


class ApplicationV1StubApiTests {

  enum class RequestCases(
    val request: IRequest,
    val response: IResponse,
  ) {
    STREAM_CREATE_NORMAL(
      request = StreamCreateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        ),
        stream = StreamCreateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      ),
      response = StreamCreateResponse(
        result = ResponseResult.SUCCESS,
        stream = StreamResponseObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          version = "0",
          active = false,
          id = "999"
        )
      )
    ),
    STREAM_CREATE_ERROR(
      request = StreamCreateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_DESCRIPTION
        ),
        stream = StreamCreateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС"
        )
      ),
      response = StreamCreateResponse(
        stream = StreamResponseObject("", "", "", "", "0", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "CREATE_ERROR", message = "неверное описание"))
      )
    ),
    STREAM_UPDATE_NORMAL(
      request = StreamUpdateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        ),
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          version = "1",
          id = "12345",
        )
      ),
      response = StreamUpdateResponse(
        result = ResponseResult.SUCCESS,
        stream = StreamResponseObject
          (
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          version = "1",
          id = "12345",
          active = false
        )
      )
    ),
    STREAM_UPDATE_ERROR(
      request = StreamUpdateRequest(
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_DESCRIPTION
        ),
        stream = StreamUpdateObject(
          classShortName = "CLIENT",
          methodShortName = "EXPORT2FNS",
          transportParams = "some transport",
          description = "Отправка информации в ФНС",
          version = "0",
          id = "12345",
        )
      ),
      response = StreamUpdateResponse(
        stream = StreamResponseObject("", "", "", "", "0", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "UPDATE_ERROR", message = "неверное описание"))
      )
    ),
    STREAM_DELETE_NORMAL(
      request = StreamDeleteRequest(
        streamId = "12345",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamDeleteResponse(
        stream = StreamResponseObject(
          id = "12345",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          version = "0",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_DELETE_ERROR(
      request = StreamDeleteRequest(
        streamId = "12345",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.CANNOT_DELETE
        )
      ),
      response = StreamDeleteResponse(
        stream = StreamResponseObject("", "", "", "", "0", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
      )
    ),
    STREAM_READ_NORMAL(
      request = StreamReadRequest(
        streamId = "12345", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamReadResponse(
        stream = StreamResponseObject(
          id = "12345",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          version = "0",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_READ_ERROR(
      request = StreamReadRequest(
        streamId = "123",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.NOT_FOUND
        )
      ),
      response = StreamReadResponse(
        stream = StreamResponseObject("", "", "", "", "0", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
      )
    ),
    STREAM_ENABLE_NORMAL(
      request = StreamEnableRequest(
        streamId = "456",
        version = "0",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamEnableResponse(
        stream = StreamResponseObject(
          id = "456",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          version = "0",
          active = true
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_ENABLE_ERROR(
      request = StreamEnableRequest(
        streamId = "789",
        version = "0",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_ID
        )
      ),
      response = StreamEnableResponse(
        stream = StreamResponseObject("", "", "", "", "0",null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "ENABLE_ERROR", message = "ошибка включения"))
      )
    ),
    STREAM_DISABLE_NORMAL(
      request = StreamDisableRequest(
        streamId = "456", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamDisableResponse(
        stream = StreamResponseObject(
          id = "456",
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          version = "0",
          active = false
        ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_DISABLE_ERROR(
      request = StreamDisableRequest(
        streamId = "789", debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_ID
        )
      ),
      response = StreamDisableResponse(
        stream = StreamResponseObject("", "", "", "", "0", null, false),
        result = ResponseResult.ERROR,
        errors = listOf(Error(code = "999", group = "DISABLE_ERROR", message = "ошибка отключения"))
      )
    ),
    STREAM_SEARCH_NORMAL(
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        ), debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamSearchResponse(
        streams =
          listOf(
            StreamResponseObject(
              id = "1",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              version = "0",
              active = false
            ), StreamResponseObject(
              id = "2",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              version = "0",
              active = false
            )
          ),
        result = ResponseResult.SUCCESS,
      )
    ),
    STREAM_SEARCH_ERROR(
      request = StreamSearchRequest(
        streamFilter = StreamSearchFilter(
          searchString = """classShorName = 'KREDS%' and methodShortName = '%'  """,
          classShortName = "DEPOSIT",
          methodShortName = "OEPN",
          active = false
        ), debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.BAD_SEARCH_STRING
        )
      ),
      response = StreamSearchResponse(
        result = ResponseResult.ERROR,
        streams = emptyList(),
        errors = listOf(Error(code = "999", group = "SEARCH_ERROR", message = "ошибка поиска"))
      )
    ),
    STREAM_ACCESSIBLE(
      request = StreamAccessibleRequest(
        externalSystemId = "11",
        debug = StreamDebug(
          mode = StreamRequestDebugMode.STUB,
          stub = StreamRequestDebugStubs.SUCCESS
        )
      ),
      response = StreamAccessibleResponse(
        streams =
          listOf(
            StreamResponseObject(
              id = "1",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              version = "0",
              active = false
            ), StreamResponseObject(
              id = "2",
              description = "Какое-то описание",
              classShortName = "SOME_CLASS",
              methodShortName = "SOME_METHOD",
              transportParams = "[1, 2, 3]",
              version = "0",
              active = false
            )
          ),
        result = ResponseResult.SUCCESS,
      )
    ),
  }

  private val applicationConfig = ApplicationConfigData(
    mode = "STUB",
    kafkaHosts = emptyList(),
    kafkaGroupId = "",
    kafkaIPStreamTopicIn = "test.in",
    kafkaIPStreamTopicOut = "test.out",
    pgUrl = "",
    pgUser = "",
    pgPassword = "",
    pgMaximumPoolSize = "",
    pgMinimumIdle = "",
    pgIdleTimeout = "",
    pgConnectionTimeout = "",
  )

  private val applicationSettings = initApplicationSettings(applicationConfig)
  private val topicPair = applicationConfig.createIPStreamTopicPair()
  private val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
  private val producer = MockProducer<String, String>(true, StringSerializer(), StringSerializer())
  private val handler = IPStreamHandler(applicationSettings = applicationSettings, consumer = consumer, producer = producer, topics = topicPair,)
  private val beginningOffset = mutableMapOf(TopicPartition("test.in", 0) to 0L)

  @ParameterizedTest
  @EnumSource
  fun test(case: RequestCases) {
    consumer.updateBeginningOffsets(beginningOffset)
    consumer.schedulePollTask {
      consumer.rebalance(Collections.singletonList(TopicPartition("test.in", 0)))
      consumer.addRecord(ConsumerRecord(topicPair.incoming, 0, 0, null, apiV1RequestSerialize(case.request)))
      handler.close()
    }

    handler.start()

    val message = producer.history().first()
    val result = apiV1ResponseDeserialize<IResponse>(message.value())
    assertEquals(case.response, result)
  }

}