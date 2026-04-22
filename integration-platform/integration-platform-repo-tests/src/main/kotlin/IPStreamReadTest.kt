package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class IPStreamReadTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun readOkTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val readRequest = RepoIPStreamIdRequest((insertResult as RepoIPStreamResponseOk).stream.id)
    val readResult = repo.readStream(readRequest)

    assertIs<RepoIPStreamResponseOk>(readResult)
    readResult.stream.apply {
      assertEquals(ipStream.classShortName, classShortName)
      assertEquals(ipStream.methodShortName, methodShortName)
      assertEquals(ipStream.transportParams, transportParams)
    }
  }

  @Test
  fun readNotFoundTest() = runTest {
    val readResult = repo.readStream(RepoIPStreamIdRequest(IPStreamId("-111")))

    assertIs<RepoIPStreamResponseError>(readResult)
    assertNotNull(readResult.errors.find { it.code == "555" })
  }

}
