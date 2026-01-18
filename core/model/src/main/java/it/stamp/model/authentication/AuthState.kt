package it.stamp.model.authentication

import it.stamp.model.user.User

sealed interface AuthState {
    data object Unknown : AuthState

    data class Authenticated(val user: User) : AuthState

    data object Unauthenticated : AuthState
}

val AuthState.currentUser: User?
    get() = if (this is AuthState.Authenticated) {
        user
    } else {
        null
    }