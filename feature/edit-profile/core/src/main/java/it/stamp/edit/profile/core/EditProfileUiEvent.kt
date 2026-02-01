package it.stamp.edit.profile.core

sealed interface EditProfileUiEvent {
    data object UpdateSuccess : EditProfileUiEvent
    data class UpdateFailure(val throwable: Throwable) : EditProfileUiEvent
}