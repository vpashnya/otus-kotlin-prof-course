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

val inMemoryRepo: IRepoStream = RepoStreamInMemory()

class IPStreamCreateInMemoryTest : IPStreamCreateTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamUpdateInMemoryTest : IPStreamUpdateTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamReadInMemoryTest : IPStreamReadTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamDeleteInMemoryTest : IPStreamDeleteTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamEnableInMemoryTest : IPStreamEnableTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamDisableInMemoryTest : IPStreamDisableTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamAccessibleInMemoryTest : IPStreamAccessibleTest() {
  override val repo: IRepoStream = inMemoryRepo
}

class IPStreamSearchInMemoryTest : IPStreamSearchTest() {
  override val repo: IRepoStream = inMemoryRepo
}
