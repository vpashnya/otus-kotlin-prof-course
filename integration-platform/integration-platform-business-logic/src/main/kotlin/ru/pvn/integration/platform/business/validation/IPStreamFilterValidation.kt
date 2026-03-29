package ru.pvn.integration.platform.business.validation

import ru.pvn.integration.platform.business.dsl.validationStreamFilter
import ru.pvn.integration.platform.lib.cor.dsl.DslPerformerChain
import ru.pvn.learning.IPContext

fun DslPerformerChain<IPContext>.validateNecessaryFieldsFilled() =
  validationStreamFilter("V011", "Не заполнено ни одно поле необходимое для поиска интеграционных потоков") {
    searchString == "" && classShortName == "" && methodShortName == ""
  }
