package ru.pvn.learning.repo

data class RepoIPStreamSearchRequest(
  val classNameLike: String? = null,
  val methodNameLike: String? = null,
  val description: String? = null,
)