import org.testcontainers.containers.ComposeContainer
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
import java.io.File
import java.time.Duration


class RepoStreamInPgTests {
  companion object {
    private val container: ComposeContainer =
      ComposeContainer(File(this::class.java.classLoader.getResource("docker-compose-pg.yml").toURI()))
        .withExposedService("pgdb", 5432)
        .withStartupTimeout(Duration.ofSeconds(300))
        .also { it.start() }


    val repoInPg: IRepoStream = RepoStreamInPg(
      PgCredentials(
        "jdbc:postgresql://localhost:5432/mydatabase",
        "myuser",
        "mypassword",
        10,
        5,
        60000,
        30000
      )
    )
  }

  class IPStreamCreateInPgTest : IPStreamCreateTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamUpdateInPgTest : IPStreamUpdateTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamReadInPgTest : IPStreamReadTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamDeleteInPgTest : IPStreamDeleteTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamEnableInPgTest : IPStreamEnableTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamDisableInPgTest : IPStreamDisableTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamAccessibleInPgTest : IPStreamAccessibleTest() {
    override val repo: IRepoStream = repoInPg
  }

  class IPStreamSearchInPgTest : IPStreamSearchTest() {
    override val repo: IRepoStream = repoInPg
  }
}
