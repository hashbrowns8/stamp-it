package it.stamp.domain.exception

sealed class AuthException(override val message: String? = null) : RuntimeException(message)

class GoogleSignInException() : AuthException()

class UserNotFoundException() : AuthException()

class NotAuthenticatedException() : AuthException()