package it.stamp.domain.exception

sealed class MissionException(override val message: String? = null) : RuntimeException(message)

class MissionNotFoundException() : MissionException()

sealed class MissionUpdateException() : MissionException() {
    class MissionAlreadyCompleted : MissionException()
    class MissionNotCompleted : MissionException()
    class Unauthorized : MissionException()
}