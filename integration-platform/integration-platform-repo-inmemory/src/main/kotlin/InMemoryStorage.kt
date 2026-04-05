import org.apache.ignite.IgniteServer
import org.apache.ignite.InitParameters
import java.nio.file.Paths
import java.nio.file.Path


class InMemoryStorage : AutoCloseable {
  private val igniteServer: IgniteServer

  constructor() {
    val url = object {}.javaClass.getResource("/ignite.cfg")
    val path = Paths.get(url.toURI())
    val server = IgniteServer.start("my-node", path, Path.of("ignite-work"))
    val initParameters = InitParameters.builder()
      .metaStorageNodeNames("my-node")
      .clusterName("cluster")
      .build()

    server.initCluster(initParameters)

    igniteServer = server
  }

  fun getSqlApi() = igniteServer.api()

  override fun close() {
    igniteServer.shutdown()
  }
}