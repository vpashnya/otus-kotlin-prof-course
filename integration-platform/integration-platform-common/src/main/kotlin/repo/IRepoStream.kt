package ru.pvn.learning.repo

interface IRepoStream {
  suspend fun createStream(stream: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun readStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun updateStream(stream: RepoIPStreamRequest): RepoIPStreamResponse
  suspend fun deleteStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun enableStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun disableStream(streamId: RepoIPStreamIdRequest): RepoIPStreamResponse
  suspend fun searchStreams(searchString: RepoIPStreamSearchRequest): RepoIPStreamsResponse
  suspend fun accessibleStream(): RepoIPStreamsResponse
}