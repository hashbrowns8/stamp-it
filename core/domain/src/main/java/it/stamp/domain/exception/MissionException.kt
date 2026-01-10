package it.stamp.domain.exception

sealed class MissionException(override val message: String? = null) : RuntimeException(message)

class MissionNotFoundException : MissionException()