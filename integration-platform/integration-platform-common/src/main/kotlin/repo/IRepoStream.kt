package ru.pvn.learning.repo

interface IRepoStream {
  suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun enableStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun disableStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse
  suspend fun accessibleStream(): RepoIPStreamsResponse

  companion object {
    val NONE = object : IRepoStream {
      override suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun enableStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun disableStream(request: RepoIPStreamRequest): RepoIPStreamResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse {
        throw NotImplementedError("Must not be used")
      }

      override suspend fun accessibleStream(): RepoIPStreamsResponse {
        throw NotImplementedError("Must not be used")
      }

    }
  }
}