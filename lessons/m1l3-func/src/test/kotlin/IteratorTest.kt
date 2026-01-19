import org.junit.jupiter.api.Assertions.assertFalse
import kotlin.test.Test
import kotlin.test.assertEquals

class IteratorTest {
  private val list = listOf("string", "1", "2")

  @Test
  fun immutableTest() {
    val iter: Iterator<String> = list.iterator()
    // iter.remove() // Not allowed
    assertEquals("string", iter.next())
  }

  @Test
  fun mutableTest() {
    val mutableList = list.toMutableList()
    val mutableIterator: MutableIterator<String> = mutableList.iterator()
    mutableIterator.next()
    mutableIterator.remove()
    assertEquals("1", mutableIterator.next())
    assertFalse(mutableList.contains("string"))
  }
}