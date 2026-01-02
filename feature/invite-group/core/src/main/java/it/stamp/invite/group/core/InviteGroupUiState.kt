package it.stamp.invite.group.core

import it.stamp.model.membership.InviteCode

sealed interface InviteGroupUiState {
    data object Loading : InviteGroupUiState

    data class Success(val inviteCode: InviteCode) : InviteGroupUiState

    data class Failure(val throwable: Throwable) : InviteGroupUiState
}