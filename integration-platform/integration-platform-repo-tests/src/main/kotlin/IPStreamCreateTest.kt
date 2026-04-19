package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

abstract class IPStreamCreateTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun createTest() = runTest {
    val result = repo.createStream(RepoIPStreamRequest(ipStream))
    assertIs<RepoIPStreamResponseOk>(result)
    result.stream.apply {
      assertNotEquals(ipStream.id, id)
      assertNotEquals(ipStream.version, version)
      assertEquals(ipStream.classShortName, classShortName)
      assertEquals(ipStream.methodShortName, methodShortName)
      assertEquals(ipStream.transportParams, transportParams)
    }
  }
}
