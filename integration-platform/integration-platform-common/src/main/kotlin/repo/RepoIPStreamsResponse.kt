package ru.pvn.learning.repo

import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPStream

interface RepoIPStreamsResponse

data class RepoIPStreamsResponseOk(
  val stream: List<IPStream>,
) : RepoIPStreamsResponse

data class RepoIPStreamsResponseFail(
  val errors: List<IPError>,
) : RepoIPStreamsResponse