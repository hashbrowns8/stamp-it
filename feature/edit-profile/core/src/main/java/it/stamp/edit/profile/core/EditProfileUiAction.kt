package it.stamp.edit.profile.core

import it.stamp.model.user.Avatar

sealed interface EditProfileUiAction
data object OnBackClick : EditProfileUiAction
data class OnAvatarChange(val avatar: Avatar) : EditProfileUiAction
data class OnDisplayNameChange(val displayName: String) : EditProfileUiAction
data class OnGroupNameChange(val groupName: String) : EditProfileUiAction
data object OnCompleteClick : EditProfileUiAction

typealias OnUiAction = (EditProfileUiAction) -> Unit