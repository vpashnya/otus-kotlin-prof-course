package ru.pvn.learning

import ru.pvn.learning.models.IPCommand
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPWorkMode
import ru.pvn.learning.stubs.IPStubs
import ru.pvn.learning.models.IPRequestId
import kotlinx.datetime.Instant
import ru.pvn.learning.models.IPExternalSystemId
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamFilter

data class IPContext(
  var command: IPCommand = IPCommand.NONE,
  var state: IPState = IPState.NONE,
  val errors: MutableList<IPError> = mutableListOf(),

  var workMode: IPWorkMode = IPWorkMode.PROD,
  var stubCase: IPStubs = IPStubs.NONE,

  var requestId: IPRequestId = IPRequestId.NONE,
  var timeStart: Instant = Instant.NONE,
  var streamRequest: IPStream = IPStream(),
  var streamFilterRequest: IPStreamFilter = IPStreamFilter(),

  var streamResponse: IPStream = IPStream(),
  var streamsResponse: MutableList<IPStream> = mutableListOf(),

  var requesterExternalSystemId: IPExternalSystemId = IPExternalSystemId.NONE
)


