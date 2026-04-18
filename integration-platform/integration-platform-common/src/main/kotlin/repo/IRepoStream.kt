package ru.pvn.learning.repo

interface IRepoStream {
  suspend fun createStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun readStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun updateStream(request: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun deleteStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun enableStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun disableStream(request: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun searchStreams(request: RepoIPStreamSearchRequest): RepoIPStreamsResponse
  suspend fun accessibleStream(): RepoIPStreamsResponse
}