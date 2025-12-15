package it.stamp.model.authentication

import it.stamp.model.user.User

sealed interface AuthenticationResult {
    data class Authenticated(
        val user: User,
        val isNewUser: Boolean = false,
    ) : AuthenticationResult

    data class Failure(val throwable: Throwable) : AuthenticationResult
}