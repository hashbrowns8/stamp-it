package it.stamp.invite.group.core

interface InviteGroupUiEvent {
    data class GetInviteCodeFailed(val throwable: Throwable) : InviteGroupUiEvent
}