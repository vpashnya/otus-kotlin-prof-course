package ru.pvn.integration.platform.lib.cor

interface IPerformer<T> {
  suspend fun exec(context: T)
}
