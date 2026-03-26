package ru.pvn.integration.platform.lib.cor

class PerformerChain<T>(
  private val performers: List<IPerformer<T>>,
) : IPerformer<T> {
  override fun exec(context: T) = performers.forEach { it.exec(context) }
}
