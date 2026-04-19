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

abstract class IPStreamDisableTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun disableOkTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val disableResult = repo.disableStream(RepoIPStreamRequest((insertResult as RepoIPStreamResponseOk).stream))

    assertIs<RepoIPStreamResponseOk>(disableResult)
    disableResult.stream.apply {
      assertNotEquals(insertResult.stream.version, version)
      assertEquals(false, active)
    }
  }

  @Test
  fun disableLockFailTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val streamForDisable = (insertResult as RepoIPStreamResponseOk).stream.copy(version = IPStreamVersion("999"))
    val disableResult = repo.disableStream(RepoIPStreamRequest(streamForDisable))

    assertIs<RepoIPStreamResponseError>(disableResult)
    disableResult.errors.find { it.code == "003999" }
  }

  @Test
  fun disableNotFoundTest() = runTest {
    val streamForDisable = ipStream.copy(id = IPStreamId("111"))
    val disableResult = repo.enableStream(RepoIPStreamRequest(streamForDisable))

    assertIs<RepoIPStreamResponseError>(disableResult)
  }

}
