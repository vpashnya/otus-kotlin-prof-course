package ru.pvn.integration.platform.lib.cor

interface IPerformer<T> {
  fun exec(context: T)
}
