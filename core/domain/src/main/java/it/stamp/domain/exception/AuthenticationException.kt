package it.stamp.domain.exception

sealed class AuthenticationException(override val message: String? = null) : RuntimeException(message)

class GoogleSignInException() : AuthenticationException()

class UserNotFoundException() : AuthenticationException()

class NotAuthenticatedException() : AuthenticationException()

class UnauthorizedException() : RuntimeException()