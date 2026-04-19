package ru.pvn.learning.repo

import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPStream

interface RepoIPStreamsResponse

data class RepoIPStreamsResponseOk(
  val streams: List<IPStream>,
) : RepoIPStreamsResponse

data class RepoIPStreamsResponseError(
  val errors: List<IPError>,
) : RepoIPStreamsResponse