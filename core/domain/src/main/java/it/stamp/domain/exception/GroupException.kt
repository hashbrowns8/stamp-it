package it.stamp.domain.exception

sealed class GroupException(override val message: String? = null) : RuntimeException(message)

class GroupNotFoundException : GroupException()