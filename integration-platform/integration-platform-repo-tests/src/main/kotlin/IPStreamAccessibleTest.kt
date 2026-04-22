package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class IPStreamAccessibleTest {
  abstract val repo: IRepoStream
  val ipStreamFirst = IPStream(
    description = "first", classShortName = "class first", methodShortName = "method first", transportParams = "params first",
  )

  val ipStreamSecond = IPStream(
    description = "second", classShortName = "class second", methodShortName = "method second", transportParams = "params second",
  )

  val ipStreamThird = IPStream(
    description = "third", classShortName = "class third", methodShortName = "method third", transportParams = "params third",
  )

  @Test
  fun accessibleOkTest() = runTest {
    (repo.searchStreams(RepoIPStreamSearchRequest()) as RepoIPStreamsResponseOk).streams.forEach { it ->
      repo.deleteStream(RepoIPStreamIdRequest(it.id))
    }

    val expect = listOf(ipStreamFirst, ipStreamSecond, ipStreamThird).map {
      (repo.createStream(RepoIPStreamRequest(it)) as RepoIPStreamResponseOk).stream
    }

    val accessibleResult = repo.accessibleStream()

    assertIs<RepoIPStreamsResponseOk>(accessibleResult)
    assertEquals(expect, accessibleResult.streams )
  }
}
