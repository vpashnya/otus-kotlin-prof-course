package homework.hard

import homework.hard.dto.Dictionary
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test

class HWHard {
  @Test
  fun hardHw() = runBlocking {
    val dictionaryApi = DictionaryApi()
    val words = FileReader.readFile().split(" ", "\n").toSet()


    val dictionaries = findWords(dictionaryApi, words, Locale.EN)

    dictionaries.filterNotNull().map { dictionary ->
      print("For word ${dictionary.word} i found examples: ")
      println(
        dictionary.meanings
          .mapNotNull { definition ->
            val r = definition.definitions
              .mapNotNull { it.example.takeIf { it?.isNotBlank() == true } }
              .takeIf { it.isNotEmpty() }
            r
          }
          .takeIf { it.isNotEmpty() }
      )
    }

    println("Finished!!!")

  }

  private suspend fun findWords(
    dictionaryApi: DictionaryApi,
    words: Set<String>,
    @Suppress("SameParameterValue") locale: Locale,
  ): List<Dictionary?> =
    coroutineScope {
      words.map { word ->
        async {
          dictionaryApi.findWord(locale, word)
        }
      }.awaitAll()
    }

  object FileReader {
    fun readFile(): String =
      File(
        this::class.java.classLoader.getResource("words.txt")?.toURI()
          ?: throw RuntimeException("Can't read file")
      ).readText()
  }
}