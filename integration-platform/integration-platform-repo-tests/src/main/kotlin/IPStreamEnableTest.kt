package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.models.IPStreamVersion
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class IPStreamEnableTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun enableOkTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val enableResult = repo.enableStream(RepoIPStreamRequest((insertResult as RepoIPStreamResponseOk).stream))

    assertIs<RepoIPStreamResponseOk>(enableResult)
    enableResult.stream.apply {
      assertNotEquals(insertResult.stream.version, version)
      assertEquals(true, active)
    }
  }

  @Test
  fun enableLockFailTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val streamForEnable = (insertResult as RepoIPStreamResponseOk).stream.copy(version = IPStreamVersion("999"))
    val enableResult = repo.enableStream(RepoIPStreamRequest(streamForEnable))

    assertIs<RepoIPStreamResponseError>(enableResult)
    enableResult.errors.find { it.code == "003999" }
  }

  @Test
  fun enableNotFoundTest() = runTest {
    val streamForEnable = ipStream.copy(id = IPStreamId("111"))
    val enableResult = repo.enableStream(RepoIPStreamRequest(streamForEnable))

    assertIs<RepoIPStreamResponseError>(enableResult)
  }

}
