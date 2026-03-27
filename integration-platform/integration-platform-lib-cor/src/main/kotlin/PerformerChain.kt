package ru.pvn.integration.platform.lib.cor

class PerformerChain<T>(
  private val conditionFunction: suspend T.() -> Boolean = { true },
  private val performers: List<IPerformer<T>>,
) : IPerformer<T> {
  override suspend fun exec(context: T) {
    if (context.conditionFunction()) {
      performers.forEach { it.exec(context) }
    }
  }
}
