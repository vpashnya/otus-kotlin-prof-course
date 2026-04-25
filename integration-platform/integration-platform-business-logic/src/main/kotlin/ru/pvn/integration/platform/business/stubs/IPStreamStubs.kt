package ru.pvn.integration.platform.business.stubs

import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPError
import ru.pvn.learning.models.IPState.*
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.models.IPStreamId
import ru.pvn.learning.stubs.IPStubs.*

fun DslPerformerChain<IPContext>.stubCreateBadDescription() =
  performer {
    conditionF { stubCase == BAD_DESCRIPTION }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "CREATE_ERROR", message = "неверное описание"))
    }
  }

fun DslPerformerChain<IPContext>.stubCreateSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
      streamResponse = streamRequest.copy(id = IPStreamId("999"))
    }
  }

fun DslPerformerChain<IPContext>.stubUpdateBadDescription() =
  performer {
    conditionF { stubCase == BAD_DESCRIPTION }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "UPDATE_ERROR", message = "неверное описание"))
    }
  }

fun DslPerformerChain<IPContext>.stubUpdateSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
      streamResponse = streamRequest
    }
  }

fun DslPerformerChain<IPContext>.stubDeleteBadId() =
  performer {
    conditionF { stubCase == BAD_ID }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
    }
  }

fun DslPerformerChain<IPContext>.stubDeleteCannot() =
  performer {
    conditionF { stubCase == CANNOT_DELETE }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "DELETE_ERROR", message = "ошибка удаления"))
    }
  }

fun DslPerformerChain<IPContext>.stubDeleteSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
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

fun DslPerformerChain<IPContext>.stubReadBadId() =
  performer {
    conditionF { stubCase == BAD_ID }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
    }
  }

fun DslPerformerChain<IPContext>.stubReadNotFound() =
  performer {
    conditionF { stubCase == NOT_FOUND }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "READ_ERROR", message = "ошибка чтения"))
    }
  }


fun DslPerformerChain<IPContext>.stubReadSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
      streamResponse = IPStream(
        id = streamRequest.id,
        description = "Какое-то описание",
        classShortName = "SOME_CLASS",
        methodShortName = "SOME_METHOD",
        transportParams = "[1, 2, 3]",
        active = false
      )
    }
  }

fun DslPerformerChain<IPContext>.stubEnableBadId() =
  performer {
    conditionF { stubCase == BAD_ID }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "ENABLE_ERROR", message = "ошибка включения"))
    }
  }

fun DslPerformerChain<IPContext>.stubEnableSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
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

fun DslPerformerChain<IPContext>.stubDisableBadId() =
  performer {
    conditionF { stubCase == BAD_ID }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "DISABLE_ERROR", message = "ошибка отключения"))

    }
  }

fun DslPerformerChain<IPContext>.stubDisableSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
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

fun DslPerformerChain<IPContext>.stubSearchBadString() =
  performer {
    conditionF { stubCase == BAD_SEARCH_STRING }
    mainF {
      state = FAILING
      errors.add(IPError(code = "999", group = "SEARCH_ERROR", message = "ошибка поиска"))
    }
  }

fun DslPerformerChain<IPContext>.stubSearchSuccess() =
  performer {
    conditionF { stubCase == SUCCESS }
    mainF {
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

fun DslPerformerChain<IPContext>.stubAccessibleSuccess() =
  performer {
    mainF {
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

