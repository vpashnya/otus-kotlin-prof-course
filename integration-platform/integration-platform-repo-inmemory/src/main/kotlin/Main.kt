package ru.pvn.learning

import InMemoryStorage
import ru.pvn.integration.platform.repo.inmemory.RepoStreamInMemory
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.repo.RepoIPStreamRequest

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main() {
  val storage = InMemoryStorage()
//  val repoStreamInMemory = RepoStreamInMemory(storage)

//  repoStreamInMemory.createStream(
//    RepoIPStreamRequest(stream = IPStream(IPStreamId.NONE, "description", "classShorName"))
//  )
}