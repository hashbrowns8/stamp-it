package it.stamp.join.group.core

sealed interface JoinGroupUiState {
    data object None : JoinGroupUiState
    data object InProgress : JoinGroupUiState
}