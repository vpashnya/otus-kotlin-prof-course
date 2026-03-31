package ru.pvn.integration.platform.business.validation

import ru.pvn.integration.platform.business.dsl.validationStreamRequest
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext
import ru.pvn.learning.models.IPStreamId

val NOT_DIGITS = "\\D+".toRegex()

fun DslPerformerChain<IPContext>.validateClassNameFilled() =
  validationStreamRequest("V001", "Не заполнено название класса для интеграционного потока") { classShortName == "" }

fun DslPerformerChain<IPContext>.validateMethodNameFilled() =
  validationStreamRequest("V002", "Не заполнено название метода для интеграционного потока") { methodShortName == "" }

fun DslPerformerChain<IPContext>.validateMethodDescriptionFilled() =
  validationStreamRequest("V003", "Не заполнено описание для интеграционного потока") { description == "" }

fun DslPerformerChain<IPContext>.validateTransportParamsFilled() =
  validationStreamRequest("V004", "Не заполнены транспортные параметры") { transportParams == "" }

fun DslPerformerChain<IPContext>.validateStreamIdFilled() =
  validationStreamRequest("V005", "Не заполнен id интеграционного потока") { id == IPStreamId.NONE }

fun DslPerformerChain<IPContext>.validateStreamIdContainOnlyDigits() =
  validationStreamRequest("V006", "Id неподходящий формат") { id.asString().matches(NOT_DIGITS) }

fun DslPerformerChain<IPContext>.validateStreamId() =
  performers {
    validateStreamIdFilled()
    validateStreamIdContainOnlyDigits()
  }