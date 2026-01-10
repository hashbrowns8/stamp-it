package it.stamp.domain.exception

sealed class SignInException(override val message: String? = null) : RuntimeException(message)

class GoogleSignInException() : SignInException()

class UserNotFoundException() : SignInException()