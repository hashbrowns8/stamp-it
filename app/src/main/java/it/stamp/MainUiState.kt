package it.stamp

import it.stamp.model.membership.Membership
import it.stamp.model.user.User

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(
        val user: User? = null,
        val membership: Membership? = null,
    ) : MainUiState

    data class Failure(val throwable: Throwable) : MainUiState
}