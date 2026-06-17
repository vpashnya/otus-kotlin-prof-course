package ru.pvn.learning.testing.machine.externalsystem

enum class MonolithClasses {
  PR_CRED, MAIN_DOCUM, DOCUMENT, KRED_CORP, KRED_PERS, DEPOSIT_PRIV, DEPOSIT_ORG, BASE_VAL_OP,
  FOLDER_PAY, COM_STATUS_PRD, LEGAL_161P, FATCA_MSG, CIT_ABONENT, CIT_IN_REQUEST, CIT_OUT_REQUEST
}

enum class MonolithMethods {
  NEW_AUTO, EDIT_AUTO, DELETE_AUTO, LIB, CALC_PARAMS, GET, PUT, LOCK
}

enum class KafkaTransportParams {
  kafka1, kafka2, kafka3, kafka4, kafka5
}

enum class RestTransportParams {
  rest1, rest2, rest3, rest4, rest5
}