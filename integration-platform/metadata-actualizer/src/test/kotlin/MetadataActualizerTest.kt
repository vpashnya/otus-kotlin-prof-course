import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.pvn.learning.MetadataActualizerKafka


class MetadataActualizerTest {

  @Test
  fun test() {
    val producer = MockProducer(true, StringSerializer(), StringSerializer())
    val actualizer = MetadataActualizerKafka(producer, "test", "test intiator")
    actualizer.sendRefresh()

    assertEquals(
      ProducerRecord("test", null, "service initiator test intiator"),
      producer.history().first()
    )
  }

}