package it.stamp.model.authentication

import it.stamp.model.user.User

interface AuthenticationState {
    data object NotAuthenticated : AuthenticationState
    data class Authenticated(val user: User) : AuthenticationState
}