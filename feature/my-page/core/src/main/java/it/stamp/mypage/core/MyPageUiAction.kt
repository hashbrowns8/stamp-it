package it.stamp.mypage.core

sealed interface MyPageUiAction

sealed interface MyProfileUiAction : MyPageUiAction {
    data object EditProfile : MyProfileUiAction
    data object ManageMembers : MyProfileUiAction
    data object InviteMember : MyProfileUiAction
    data object JoinGroup : MyProfileUiAction
    data object LeaveGroup : MyProfileUiAction
    data object DeleteAccount : MyProfileUiAction
    data object SignOut : MyProfileUiAction
}