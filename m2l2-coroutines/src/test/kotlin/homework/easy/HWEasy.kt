package homework.easy

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class HWEasy {

  @Test
  fun easyHw() = runBlocking {
    val numbers = generateNumbers()
    val toFind = 10
    val toFindOther = 1000

    val findingFirst = async { findNumberInList(toFind, numbers) }
    val findingSecond = async { findNumberInList(toFind, numbers) }
    val foundNumbers = listOf(
      findingFirst.await(),
      findingSecond.await()
    )

    foundNumbers.forEach {
      if (it != -1) {
        println("Your number $it found!")
      } else {
        println("Not found number $toFind || $toFindOther")
      }
    }
  }
}