package it.stamp.home

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.UserId

sealed interface HomeUiAction {
    data object OnNotificationsClick : HomeUiAction
    data object OnGroupOnboardingClick : HomeUiAction
    data object OnViewMyMissionsMoreClick : HomeUiAction
    data object OnRequestNewMissionClick : HomeUiAction
    data class OnViewMemberMissionsMoreClick(val groupId: GroupId) : HomeUiAction
    data class OnAssignNewMissionClick(val assigneeId: UserId?) : HomeUiAction
}

typealias OnUiAction = (HomeUiAction) -> Unit