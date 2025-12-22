package it.stamp.domain.exception

sealed class GroupException : Exception()

class GroupNotFoundException : GroupException()