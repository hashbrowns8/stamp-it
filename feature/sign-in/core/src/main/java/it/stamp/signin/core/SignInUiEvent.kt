package it.stamp.signin.core

import it.stamp.model.user.User

sealed interface SignInUiEvent {
    data class SignedIn(val user: User) : SignInUiEvent
    data class SignInFailed(val throwable: Throwable) : SignInUiEvent
}
