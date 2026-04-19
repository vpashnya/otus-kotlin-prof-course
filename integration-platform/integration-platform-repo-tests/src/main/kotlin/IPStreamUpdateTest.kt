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

abstract class IPStreamUpdateTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun updateOkTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val streamForUpdate = (insertResult as RepoIPStreamResponseOk).stream.copy(
      methodShortName = "updated method name",
      classShortName = "updated class name",
      description = "updated description",
      transportParams = "updated transport params"
    )
    val updateResult = repo.updateStream(RepoIPStreamRequest(streamForUpdate))

    assertIs<RepoIPStreamResponseOk>(updateResult)
    updateResult.stream.apply {
      assertNotEquals(streamForUpdate.version, version)
      assertEquals(streamForUpdate.classShortName, classShortName)
      assertEquals(streamForUpdate.methodShortName, methodShortName)
      assertEquals(streamForUpdate.transportParams, transportParams)
    }
  }

  @Test
  fun updateLockFailTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val streamForUpdate = (insertResult as RepoIPStreamResponseOk).stream.copy(version = IPStreamVersion("999"))
    val updateResult = repo.updateStream(RepoIPStreamRequest(streamForUpdate))

    assertIs<RepoIPStreamResponseError>(updateResult)
    updateResult.errors.find { it.code == "003999" }
  }

  @Test
  fun updateNotFoundTest() = runTest {
    val streamForUpdate = ipStream.copy(id = IPStreamId("111"))
    val updateResult = repo.updateStream(RepoIPStreamRequest(streamForUpdate))

    assertIs<RepoIPStreamResponseError>(updateResult)
  }

}
