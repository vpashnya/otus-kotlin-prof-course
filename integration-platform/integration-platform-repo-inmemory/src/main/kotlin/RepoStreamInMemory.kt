package ru.pvn.integration.platform.repo.inmemory

import InMemoryStorage
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.IRepoStream
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamRequest
import ru.pvn.learning.repo.RepoIPStreamResponse
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponse
import java.util.concurrent.atomic.AtomicInteger

class RepoStreamInMemory : IRepoStream {
  private val seqId = AtomicInteger(0)

  private val storage: InMemoryStorage

  constructor(storage: InMemoryStorage) {

    this.storage = storage
  }

  override suspend fun createStream(stream: RepoIPStreamRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun readStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun updateStream(stream: RepoIPStreamRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun deleteStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun enableStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun disableStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse {
    TODO("Not yet implemented")
  }

  override suspend fun searchStreams(searchString: RepoIPStreamSearchRequest): RepoIPStreamsResponse {
    TODO("Not yet implemented")
  }

  override suspend fun accessibleStream(): RepoIPStreamsResponse {
    TODO("Not yet implemented")
  }
}