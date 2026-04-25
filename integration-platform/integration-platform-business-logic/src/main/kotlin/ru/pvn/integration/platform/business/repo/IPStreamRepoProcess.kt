package ru.pvn.integration.platform.business.repo

import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPState.FAILING
import ru.pvn.learning.models.IPStream
import ru.pvn.learning.repo.RepoIPStreamIdRequest
import ru.pvn.learning.repo.RepoIPStreamRequest
import ru.pvn.learning.repo.RepoIPStreamResponse
import ru.pvn.learning.repo.RepoIPStreamResponseDeleteOk
import ru.pvn.learning.repo.RepoIPStreamResponseError
import ru.pvn.learning.repo.RepoIPStreamResponseOk
import ru.pvn.learning.repo.RepoIPStreamSearchRequest
import ru.pvn.learning.repo.RepoIPStreamsResponse
import ru.pvn.learning.repo.RepoIPStreamsResponseError
import ru.pvn.learning.repo.RepoIPStreamsResponseOk


fun DslPerformerChain<IPContext>.createInRepo() =
  processSingleInRepo {
    streamRepo.createStream(RepoIPStreamRequest(streamRequestToRepo))
  }

fun DslPerformerChain<IPContext>.updateInRepo() =
  processSingleInRepo {
    streamRepo.updateStream(RepoIPStreamRequest(streamRequestToRepo))
  }

fun DslPerformerChain<IPContext>.readInRepo() =
  processSingleInRepo {
    streamRepo.readStream(RepoIPStreamIdRequest(streamRequestToRepo.id))
  }

fun DslPerformerChain<IPContext>.enableInRepo() =
  processSingleInRepo {
    streamRepo.enableStream(RepoIPStreamRequest(streamRequestToRepo))
  }

fun DslPerformerChain<IPContext>.disableInRepo() =
  processSingleInRepo {
    streamRepo.disableStream(RepoIPStreamRequest(streamRequestToRepo))
  }

fun DslPerformerChain<IPContext>.deleteInRepo() =
  performer {
    mainF {
      val result = streamRepo.deleteStream(RepoIPStreamIdRequest(streamRequestToRepo.id))
      when (result) {
        is RepoIPStreamResponseDeleteOk -> {
          streamResponseFromRepo = IPStream(id = result.streamId)
        }

        is RepoIPStreamResponseError -> {
          state = FAILING
          errors.addAll(result.errors)
        }
      }
    }
  }

fun DslPerformerChain<IPContext>.searchInRepo() =
  processManyInRepo {
    streamFilterRequestToRepo.run {
      streamRepo.searchStreams(
        RepoIPStreamSearchRequest(classShortName, methodShortName, description)
      )
    }
  }

fun DslPerformerChain<IPContext>.accessibleInRepo() =
  processManyInRepo {
    streamRepo.accessibleStream()
  }

fun DslPerformerChain<IPContext>.processSingleInRepo(func: suspend IPContext.() -> RepoIPStreamResponse) =
  performer {
    mainF {
      val result = func()
      when (result) {
        is RepoIPStreamResponseOk -> {
          streamResponseFromRepo = result.stream
        }
        is RepoIPStreamResponseError -> {
          state = FAILING
          errors.addAll(result.errors)
        }
      }
    }
  }

fun DslPerformerChain<IPContext>.processManyInRepo(func: suspend IPContext.() -> RepoIPStreamsResponse) =
  performer {
    mainF {
      val result = func()
      when (result) {
        is RepoIPStreamsResponseOk -> {
          streamsResponseFromRepo = result.streams.toMutableList()
        }
        is RepoIPStreamsResponseError -> {
          state = FAILING
          errors.addAll(result.errors)
        }
      }
    }
  }
