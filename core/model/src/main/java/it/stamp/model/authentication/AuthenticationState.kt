package it.stamp.model.authentication

import it.stamp.model.ids.UserId

sealed interface AuthenticationState {

    data object Initializing : AuthenticationState

    data class Authenticated(val userId: UserId) : AuthenticationState

    data object Unauthenticated : AuthenticationState
}