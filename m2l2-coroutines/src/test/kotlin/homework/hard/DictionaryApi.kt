package homework.hard

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import homework.hard.dto.Dictionary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.Response
import kotlin.collections.firstOrNull

class DictionaryApi(
  private val objectMapper: ObjectMapper = jacksonObjectMapper()
) {

  suspend fun findWord(locale: Locale, word: String): Dictionary? { // make something with context
    val url = "$DICTIONARY_API/${locale.code}/$word"
    println("Searching $url")

    val result = withContext(Dispatchers.IO) {
      getBody(HttpClient.get(url).execute())?.firstOrNull()
    }

    return result
  }


  private fun getBody(response: Response): List<Dictionary>? {
    if (!response.isSuccessful) {
      return emptyList()
    }

    return response.body?.let {
      objectMapper.readValue(it.string())
    }
  }
}