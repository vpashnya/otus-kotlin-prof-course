package ru.pvn.learning.repo

data class RepoIPStreamSearchRequest(
  val classNameLike : String?,
  val methodNameLike : String?,
  val active: Boolean?
)