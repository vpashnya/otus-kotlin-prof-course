import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import SqlWhereBuilder.ConditionAggregator.*

// Реализуйте dsl для составления sql запроса, чтобы все тесты стали зелеными.
class HomeWorkDSLTest {

  private fun checkSQL(expected: String, sql: SqlQueryBuilder) {
    assertEquals(expected, sql.build())
  }

  @Test
  fun `simple select all from table`() {
    val expected = "select * from table"

    val real = query {
      from("table")
    }

    checkSQL(expected, real)
  }

  @Test
  fun `check that select can't be used without table`() {
    assertFailsWith<Exception> {
      query {
        select("col_a")
      }.build()
    }
  }

  @Test
  fun `select certain columns from table`() {
    val expected = "select col_a, col_b from table"

    val real = query {
      select("col_a", "col_b")
      from("table")
    }

    checkSQL(expected, real)
  }

  @Test
  fun `select certain columns from table 1`() {
    val expected = "select col_a, col_b from table"

    val real = query {
      select("col_a", "col_b")
      from("table")
    }

    checkSQL(expected, real)
  }

  /**
   * __eq__ is "equals" function. Must be one of char:
   *  - for strings - "="
   *  - for numbers - "="
   *  - for null - "is"
   */
  @Test
  fun `select with complex where condition with one condition`() {
    val expected = "select * from table where col_a = 'id'"

    val real = query {
      from("table")
      where { "col_a" eq "id" }
    }

    checkSQL(expected, real)
  }

  /**
   * __nonEq__ is "non equals" function. Must be one of chars:
   *  - for strings - "!="
   *  - for numbers - "!="
   *  - for null - "!is"
   */
  @Test
  fun `select with complex where condition with two conditions`() {
    val expected = "select * from table where col_a != 0"

    val real = query {
      from("table")
      where {
        "col_a" nonEq 0
      }
    }

    checkSQL(expected, real)
  }

  @Test
  fun `when 'or' conditions are specified then they are respected`() {
    val expected = "select * from table where (col_a = 4 or col_b !is null)"

    val real = query {
      from("table")
      where {
        or {
          "col_a" eq 4
          "col_b" nonEq null
        }
      }
    }

    checkSQL(expected, real)
  }
}

fun query(sqlQueryBlock: SqlQueryBuilder.() -> Unit): SqlQueryBuilder {
  val sqlQueryBuilder = SqlQueryBuilder()

  sqlQueryBuilder.sqlQueryBlock()

  return sqlQueryBuilder
}

class SqlQueryBuilder {

  var columns: String? = null
  var tableName: String? = null
  var sqlWhereBuilder = SqlWhereBuilder()

  fun select(vararg columnAlies: String) {
    columns = columnAlies.joinToString(", ")
  }

  fun from(tableName: String) {
    this.tableName = tableName
  }

  fun where(whereBlock: SqlWhereBuilder.() -> String) {
    sqlWhereBuilder.whereBlock()
  }

  fun build(): String {
    val queryText = StringBuilder()
    queryText.append("select ${columns ?: "*"} ")
    queryText.append("from ${tableName ?: throw Exception("query without table is bad query")}")
    queryText.append(sqlWhereBuilder.build())

    return queryText.toString()
  }

}


class SqlWhereBuilder {
  val conditions = mutableListOf<String>()

  infix fun String.eq(rightPart: String): String {
    val condition = "$this = '$rightPart'"
    conditions.add(condition)
    return condition
  }

  infix fun String.eq(rightPart: Number): String {
    val condition = "$this = $rightPart"
    conditions.add(condition)
    return condition
  }

  infix fun String.nonEq(rightPart: Number): String {
    val condition = "$this != $rightPart"
    conditions.add(condition)
    return condition
  }

  infix fun String.nonEq(rightPart: Any?): String {
    val condition = if (rightPart == null) {
      "$this !is null"
    } else {
      "$this != $rightPart"
    }
    conditions.add(condition)
    return condition
  }

  fun or(condition: SqlWhereBuilder.() -> String) = conditionAggregator(condition, OR)
  fun and(condition: SqlWhereBuilder.() -> String) = conditionAggregator(condition, AND)
  fun conditionAggregator(condition: SqlWhereBuilder.() -> String, aggregator: ConditionAggregator): String {
    val sqlWhereBuilder = SqlWhereBuilder()
    sqlWhereBuilder.condition()
    val condition = "(${sqlWhereBuilder.build(false, aggregator)})"
    conditions.add(condition)
    return condition
  }

  fun build(withWhere: Boolean = true, aggregator: ConditionAggregator = AND): String {
    val whereText = StringBuilder()
    conditions.forEachIndexed { index, condition ->
      if (index == 0) {
        if (withWhere) {
          whereText.append(" where ")
        }
      }
      if ((index != condition.lastIndex) && (index != 0)) {
        whereText.append(aggregator.text)
      }
      whereText.append(condition)
    }
    return whereText.toString()
  }

  enum class ConditionAggregator(val text: String) {
    OR(" or "),
    AND(" and ")
  }
}



