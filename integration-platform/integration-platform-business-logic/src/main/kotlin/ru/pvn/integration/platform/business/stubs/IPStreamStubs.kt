package ru.pvn.integration.platform.business.stubs

import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.stubs.IPStubs.*

fun IPContext.createStubs() =
  when (stubCase) {
    BAD_DESCRIPTION -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "CREATE_ERROR", message = "неверное описание"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = streamRequest.copy(id = IPStreamId("999"))
    }
  }


fun IPContext.updateStubs() =
  when (stubCase) {
    BAD_DESCRIPTION -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "UPDATE_ERROR", message = "неверное описание"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = streamRequest
    }
  }


fun IPContext.deleteStubs() =
  when (stubCase) {
    BAD_ID, CANNOT_DELETE -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = IPStream(
        id = streamRequest.id,
        description = "Какое-то описание",
        classShortName = "SOME_CLASS",
        methodShortName = "SOME_METHOD",
        transportParams = "[1, 2, 3]",
        active = false,
      )
    }
  }


fun IPContext.readStubs() =
  when (stubCase) {
    BAD_ID, NOT_FOUND -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = IPStream(
        id = streamRequest.id,
        description = "Какое-то описание",
        classShortName = "SOME_CLASS",
        methodShortName = "SOME_METHOD",
        transportParams = "[1, 2, 3]",
        active = false,
      )
    }
  }


fun IPContext.enableStubs() =
  when (stubCase) {
    BAD_ID -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "ENABLE_ERROR", message = "ошибка включения"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = IPStream(
        id = streamRequest.id,
        description = "Какое-то описание",
        classShortName = "SOME_CLASS",
        methodShortName = "SOME_METHOD",
        transportParams = "[1, 2, 3]",
        active = true,
      )
    }
  }


fun IPContext.disableStubs() =
  when (stubCase) {
    BAD_ID -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "DISABLE_ERROR", message = "ошибка отключения"))
    }

    else -> {
      state = IPState.RUNNING
      streamResponse = IPStream(
        id = streamRequest.id,
        description = "Какое-то описание",
        classShortName = "SOME_CLASS",
        methodShortName = "SOME_METHOD",
        transportParams = "[1, 2, 3]",
        active = false,
      )
    }
  }


fun IPContext.searchStubs() =
  when (stubCase) {
    BAD_SEARCH_STRING -> {
      state = IPState.FAILING
      errors.add(IPError(code = "999", group = "SEARCH_ERROR", message = "ошибка поиска"))
    }

    else -> {
      state = IPState.RUNNING
      streamsResponse = mutableListOf(
        IPStream(
          id = IPStreamId("1"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false,
        ),
        IPStream(
          id = IPStreamId("2"),
          description = "Какое-то описание",
          classShortName = "SOME_CLASS",
          methodShortName = "SOME_METHOD",
          transportParams = "[1, 2, 3]",
          active = false,
        ),
      )
    }
  }

fun IPContext.searchAccessible() {
  state = IPState.RUNNING
  streamsResponse = mutableListOf(
    IPStream(
      id = IPStreamId("1"),
      description = "Какое-то описание",
      classShortName = "SOME_CLASS",
      methodShortName = "SOME_METHOD",
      transportParams = "[1, 2, 3]",
      active = false,
    ),
    IPStream(
      id = IPStreamId("2"),
      description = "Какое-то описание",
      classShortName = "SOME_CLASS",
      methodShortName = "SOME_METHOD",
      transportParams = "[1, 2, 3]",
      active = false,
    ),
  )
}
