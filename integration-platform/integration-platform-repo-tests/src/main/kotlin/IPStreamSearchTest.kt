package ru.pvn.learning

import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamRequest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponseOk
import kotlin.random.Random
import kotlin.test.assertEquals


abstract class IPStreamSearchTest {
  abstract val repo: IRepoStream

  val uniqueValue = Random.nextInt(1000)
  val ipStreamFirst = IPStream(classShortName = "${uniqueValue}class first", methodShortName = "${uniqueValue}method first", description = "${uniqueValue}some content")
  val ipStreamSecond = IPStream(classShortName = "${uniqueValue}class second", methodShortName = "${uniqueValue}method second")
  val ipStreamThird = IPStream(classShortName = "${uniqueValue}class third", methodShortName = "${uniqueValue}method third", description = "${uniqueValue}some content")
  val ipStreamThirdMany = IPStream(classShortName = "${uniqueValue}class thirdmany", methodShortName = "${uniqueValue}method third")

  @Test
  fun searchOkTest() = runTest {
    val expectFirst = (repo.createStream(RepoIPStreamRequest(ipStreamFirst)) as RepoIPStreamResponseOk).stream
    val expectSecond = (repo.createStream(RepoIPStreamRequest(ipStreamSecond)) as RepoIPStreamResponseOk).stream
    val expectThird = (repo.createStream(RepoIPStreamRequest(ipStreamThird)) as RepoIPStreamResponseOk).stream
    val expectThirdMany = (repo.createStream(RepoIPStreamRequest(ipStreamThirdMany)) as RepoIPStreamResponseOk).stream

    assertEquals(listOf(expectFirst),(repo.searchStreams(RepoIPStreamSearchRequest(classNameLike = "${uniqueValue}class first")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectSecond),(repo.searchStreams(RepoIPStreamSearchRequest(methodNameLike = "${uniqueValue}method second")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectThird, expectThirdMany),(repo.searchStreams(RepoIPStreamSearchRequest(classNameLike = "%${uniqueValue}%third%")) as RepoIPStreamsResponseOk).streams)
    assertEquals(listOf(expectFirst, expectThird),(repo.searchStreams(RepoIPStreamSearchRequest(description = "%${uniqueValue}some content%")) as RepoIPStreamsResponseOk).streams)

  }
}
