package ru.pvn.integration.platform.lib.cor

class PerformerSingle<T>(
  val conditionFunction: suspend T.() -> Boolean,
  val mainFunction: suspend T.() -> Unit,
  val exceptionFunction: suspend T.(Exception) -> Unit,
) : IPerformer<T> {
  override suspend fun exec(context: T) =
    context.run {
      try {
        if (conditionFunction())
          mainFunction()
      } catch (e: Exception) {
        exceptionFunction(e)
      }
    }
}
