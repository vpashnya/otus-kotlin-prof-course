import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import ru.pvn.integration.platform.business.metadata.metadataRefresh
import ru.pvn.integration.platform.lib.cor.dsl.createChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.actualizer.MetadataActualizer

class MetaDataActualizerTest {

  @Test
  fun refreshMetadataTest() = runBlocking {
    val metadataActualizer = mock<MetadataActualizer>()
    val context = IPContext(metadataActualizer = metadataActualizer)

    createChain {
      metadataRefresh()
    }.exec(context)
    verify(metadataActualizer, times(1)).sendRefresh()
  }
}