package it.stamp.domain.exception

sealed class UserException : Exception()

class UserNotFoundException : UserException()