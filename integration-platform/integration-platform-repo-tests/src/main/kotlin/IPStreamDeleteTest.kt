package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamResponseDeleteOk
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import kotlin.test.assertIs

abstract class IPStreamDeleteTest {
  abstract val repo: IRepoStream

  private val ipStream = IPStream(
    description = "test description",
    classShortName = "test class",
    methodShortName = "test methods",
    transportParams = "test params",
  )

  @Test
  fun deleteOkTest() = runTest {
    val insertResult = repo.createStream(RepoIPStreamRequest(ipStream))
    val deleteResult = (insertResult as RepoIPStreamResponseOk).stream.run {
      repo.deleteStream(RepoIPStreamIdRequest(id))
    }

    assertIs<RepoIPStreamResponseDeleteOk>(deleteResult)
  }

  @Test
  fun deleteFailTest() = runTest {
    val deleteResult = repo.deleteStream(RepoIPStreamIdRequest(IPStreamId("-111")))
    assertIs<RepoIPStreamResponseError>(deleteResult)
    deleteResult.errors.find { it.code == "555" }
  }

}
