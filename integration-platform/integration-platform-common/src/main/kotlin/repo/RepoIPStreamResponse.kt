package ru.pvn.learning.repo

import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId

interface RepoIPStreamResponse

data class RepoIPStreamResponseOk(
  val stream: IPStream,
) : RepoIPStreamResponse

data class RepoIPStreamResponseError(
  val errors: List<IPError>,
) : RepoIPStreamResponse

data class RepoIPStreamResponseDeleteOk(
  val streamId: IPStreamId,
) : RepoIPStreamResponse

