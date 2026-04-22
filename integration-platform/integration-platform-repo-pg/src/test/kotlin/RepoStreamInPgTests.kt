
import ru.pvn.learning.IPStreamAccessibleTest
import ru.pvn.learning.IPStreamCreateTest
import ru.pvn.learning.IPStreamDeleteTest
import ru.pvn.learning.IPStreamDisableTest
import ru.pvn.learning.IPStreamEnableTest
import ru.pvn.learning.IPStreamReadTest
import ru.pvn.learning.IPStreamSearchTest
import ru.pvn.learning.IPStreamUpdateTest
import ru.pvn.learning.PgCredentials
import ru.pvn.learning.RepoStreamInPg
import ru.pvn.learning.repo.IRepoStream

val inPgRepo: IRepoStream = RepoStreamInPg(
  PgCredentials(
    "jdbc:postgresql://localhost:5432/mydatabase",
    "myuser",
    "mypassword"
  )
)

class IPStreamCreateInPgTest : IPStreamCreateTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamUpdateInPgTest : IPStreamUpdateTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamReadInPgTest : IPStreamReadTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamDeleteInPgTest : IPStreamDeleteTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamEnableInPgTest : IPStreamEnableTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamDisableInPgTest : IPStreamDisableTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamAccessibleInPgTest : IPStreamAccessibleTest() {
  override val repo: IRepoStream = inPgRepo
}

class IPStreamSearchInPgTest : IPStreamSearchTest() {
  override val repo: IRepoStream = inPgRepo
}
