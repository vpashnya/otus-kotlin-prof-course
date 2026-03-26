package ru.pvn.integration.platform.lib.cor

class PerformerSingle<T>(
  val conditionFunction: T.() -> Boolean,
  val mainFunction: T.() -> Unit,
  val exceptionFunction: T.(Exception) -> Unit,
) : IPerformer<T> {
  override fun exec(context: T) =
    context.run {
      try {
        if (conditionFunction())
          mainFunction()
      } catch (e: Exception) {
        exceptionFunction(e)
      }
    }
}
