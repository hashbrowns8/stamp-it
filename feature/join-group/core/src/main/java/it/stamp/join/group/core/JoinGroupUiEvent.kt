package it.stamp.join.group.core

import it.stamp.model.ids.GroupId

sealed interface JoinGroupUiEvent {
    data object InvalidInviteCode : JoinGroupUiEvent
    data object AlreadyMember : JoinGroupUiEvent
    data class DataLossWarning(val targetGroupId: GroupId) : JoinGroupUiEvent
    data object JoinSuccess : JoinGroupUiEvent
    data class JoinFailed(val throwable: Throwable) : JoinGroupUiEvent
}