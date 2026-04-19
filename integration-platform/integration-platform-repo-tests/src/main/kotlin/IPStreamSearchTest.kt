package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import kotlin.test.assertEquals


abstract class IPStreamSearchTest {
  abstract val repo: IRepoStream

  val ipStreamFirst = IPStream(classShortName = "class first", methodShortName = "method first", description = "some content")
  val ipStreamSecond = IPStream(classShortName = "class second", methodShortName = "method second")
  val ipStreamThird = IPStream(classShortName = "class third", methodShortName = "method third", description = "some content")
  val ipStreamThirdMany = IPStream(classShortName = "class thirdmany", methodShortName = "method third")

  @Test
  fun searchOkTest() = runTest {
    val expectFirst = (repo.createStream(RepoIPStreamRequest(ipStreamFirst)) as RepoIPStreamResponseOk).stream
    val expectSecond = (repo.createStream(RepoIPStreamRequest(ipStreamSecond)) as RepoIPStreamResponseOk).stream
    val expectThird = (repo.createStream(RepoIPStreamRequest(ipStreamThird)) as RepoIPStreamResponseOk).stream
    val expectThirdMany = (repo.createStream(RepoIPStreamRequest(ipStreamThirdMany)) as RepoIPStreamResponseOk).stream

    assertEquals(listOf(expectFirst),(repo.searchStreams(RepoIPStreamSearchRequest(classNameLike = "class first")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectSecond),(repo.searchStreams(RepoIPStreamSearchRequest(methodNameLike = "method second")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectThird, expectThirdMany),(repo.searchStreams(RepoIPStreamSearchRequest(classNameLike = "%third%")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectFirst, expectThird),(repo.searchStreams(RepoIPStreamSearchRequest(description = "%some content%")) as RepoIPStreamsResponseOk).streams)

  }
}
