package ru.pvn.learning.exceptions

import ru.pvn.learning.models.IPCommand

class UnknownIPCommand(command: IPCommand) : Throwable("Wrong command $command at mapping toTransport stage")
