package it.stamp.invite.group

interface InviteGroupUiEvent {
    data class GetInviteCodeFailed(val throwable: Throwable) : InviteGroupUiEvent
}