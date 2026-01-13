package it.stamp.join.group.core

import it.stamp.model.membership.Group

sealed interface JoinGroupUiEvent {
    data object InvalidCode : JoinGroupUiEvent

    data object AlreadyInGroup : JoinGroupUiEvent

    data class RequestDataLossConsent(
        val leavingGroup: Group,
        val joiningGroup: Group,
    ) : JoinGroupUiEvent

    data class JoinGroupSuccess(val group: Group) : JoinGroupUiEvent

    data class JoinGroupFailure(val throwable: Throwable) : JoinGroupUiEvent
}