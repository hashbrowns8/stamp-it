package it.stamp.domain.exception

sealed class MissionException : Exception()

class MissionNotFoundException : MissionException()