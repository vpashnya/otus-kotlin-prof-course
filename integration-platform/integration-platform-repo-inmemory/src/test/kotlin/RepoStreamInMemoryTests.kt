import ru.pvn.integration.platform.repo.inmemory.RepoStreamInMemory
import ru.pvn.learning.IPStreamAccessibleTest
import ru.pvn.learning.IPStreamCreateTest
import ru.pvn.learning.IPStreamDeleteTest
import ru.pvn.learning.IPStreamDisableTest
import ru.pvn.learning.IPStreamEnableTest
import ru.pvn.learning.IPStreamReadTest
import ru.pvn.learning.IPStreamSearchTest
import ru.pvn.learning.IPStreamUpdateTest
import ru.pvn.learning.repo.IRepoStream

class RepoStreamInMemoryTests {
  companion object {
    val repoInMemory = RepoStreamInMemory()
  }

  class IPStreamCreateInMemoryTest : IPStreamCreateTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamUpdateInMemoryTest : IPStreamUpdateTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamReadInMemoryTest : IPStreamReadTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamDeleteInMemoryTest : IPStreamDeleteTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamEnableInMemoryTest : IPStreamEnableTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamDisableInMemoryTest : IPStreamDisableTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamAccessibleInMemoryTest : IPStreamAccessibleTest() {
    override val repo: IRepoStream = repoInMemory
  }

  class IPStreamSearchInMemoryTest : IPStreamSearchTest() {
    override val repo: IRepoStream = repoInMemory
  }
}

