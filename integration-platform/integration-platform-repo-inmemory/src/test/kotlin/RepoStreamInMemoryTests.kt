import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import ru.pvn.integration.platform.repo.inmemory.RepoStreamInMemory
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.repo.RepoIPStreamRequest

class RepoStreamInMemoryTests {
  companion object {
    val storage: InMemoryStorage by lazy { InMemoryStorage() }
    val repoStreamInMemory: RepoStreamInMemory by lazy { RepoStreamInMemory(storage) }
  }

  @Test
  fun createTest() = runTest {
    repoStreamInMemory.createStream(
      RepoIPStreamRequest(stream = IPStream(IPStreamId.NONE, "description", "classShorName"))
    )
  }

  @Test
  fun updateTest() = runTest {
    repoStreamInMemory.createStream(
      RepoIPStreamRequest(stream = IPStream(IPStreamId.NONE, "description", "classShorName"))
    )
  }

  @Test
  fun readTest() = runTest {
    repoStreamInMemory.createStream(
      RepoIPStreamRequest(stream = IPStream(IPStreamId.NONE, "description", "classShorName"))
    )
  }

  @Test
  fun deleteTest() = runTest {
    repoStreamInMemory.createStream(
      RepoIPStreamRequest(stream = IPStream(IPStreamId.NONE, "description", "classShorName"))
    )
  }

}