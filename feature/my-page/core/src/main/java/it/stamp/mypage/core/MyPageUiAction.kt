package it.stamp.mypage.core

sealed interface MyPageUiAction

sealed interface MyProfileUiAction : MyPageUiAction {
    data object OnEditProfileClick : MyProfileUiAction
    data object OnManageMembersClick : MyProfileUiAction
    data object OnInviteMemberClick : MyProfileUiAction
    data object OnJoinGroupClick : MyProfileUiAction
    data class OnLeaveGroupClick(val groupName: String) : MyProfileUiAction
    data object OnDeleteAccountClick : MyProfileUiAction
    data object OnSignOutClick : MyProfileUiAction
}