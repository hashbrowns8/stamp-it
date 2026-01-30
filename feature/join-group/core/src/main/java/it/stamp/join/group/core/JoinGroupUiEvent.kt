package it.stamp.join.group.core

import it.stamp.model.membership.Group

sealed interface JoinGroupUiEvent {
    data object InvalidCode : JoinGroupUiEvent

    data object AlreadyInGroup : JoinGroupUiEvent

    data class DataLossConsentRequired(
        val leavingGroup: Group,
        val joiningGroup: Group,
    ) : JoinGroupUiEvent

    data class JoinGroupSucceeded(val group: Group) : JoinGroupUiEvent

    data class JoinGroupFailed(val throwable: Throwable) : JoinGroupUiEvent
}