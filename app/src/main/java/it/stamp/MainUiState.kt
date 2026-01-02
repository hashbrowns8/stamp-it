package it.stamp

import it.stamp.model.user.User

sealed interface MainUiState {
    data object Loading : MainUiState

    data class Success(val user: User?) : MainUiState

    data class Failure(val throwable: Throwable) : MainUiState
}