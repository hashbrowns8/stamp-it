package it.stamp.home.core

import it.stamp.model.ids.GroupId
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId

sealed interface HomeUiAction
data object OnNotificationsClick : HomeUiAction
data object OnInviteGroupClick : HomeUiAction
data object OnJoinGroupClick : HomeUiAction
data object OnViewMoreMyMissionsClick : HomeUiAction
data object OnRequestNewMissionClick : HomeUiAction
data class OnViewMoreMemberMissionsClick(val groupId: GroupId) : HomeUiAction
data class OnAssignNewMissionClick(val assigneeId: UserId?) : HomeUiAction
data class OnCompleteMissionClick(val missionId: MissionId) : HomeUiAction

typealias OnUiAction = (HomeUiAction) -> Unit