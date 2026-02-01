package it.stamp.edit.profile.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.domain.usecase.leadership.RenameGroupUseCase
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.domain.usecase.user.UpdateUserProfileUseCase
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyGroupUseCase: ObserveMyGroupUseCase,
    observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val renameGroupUseCase: RenameGroupUseCase,
) : ViewModel() {

    private val original = observeCurrentUserUseCase()
        .filterNotNull()
        .combine(observeMyGroupUseCase().filterNotNull()) { user, group ->
            Profile(
                avatar = user.avatar,
                displayName = user.displayName.value,
                groupName = group.name,
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val canRenameGroup = observeMyMembershipUseCase()
        .filterNotNull()
        .map { membership -> membership.isLeader }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val draft = MutableStateFlow<Profile?>(null)

    val uiState: StateFlow<EditProfileUiState> = combine(
        original,
        draft,
        canRenameGroup,
    ) { original, draft, canRenameGroup ->
        if (original == null) return@combine EditProfileUiState.Loading

        EditProfileUiState.Success(
            original,
            draft = draft ?: original,
            canRenameGroup,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, EditProfileUiState.Loading)

    fun updateAvatar(avatar: Avatar) {
        draft.update { draft ->
            (draft ?: original.value)?.copy(avatar = avatar)
        }
    }

    fun updateDisplayName(displayName: String) {
        draft.update { draft ->
            (draft ?: original.value)?.copy(displayName = displayName)
        }
    }

    fun updateGroupName(groupName: String) {
        draft.update { draft ->
            (draft ?: original.value)?.copy(groupName = groupName)
        }
    }

    private val _uiEvent = MutableSharedFlow<EditProfileUiEvent>()
    val uiEvent: SharedFlow<EditProfileUiEvent> = _uiEvent.asSharedFlow()

    fun complete() {
        viewModelScope.launch {
            val state = uiState.value as? EditProfileUiState.Success ?: return@launch

            if (!state.canComplete) return@launch

            awaitAll(
                async {
                    if (state.isUserProfileChanged) {
                        updateUserProfileUseCase(state.avatar, DisplayName(state.displayName))
                    } else {
                        Result.success(Unit)
                    }
                },
                async {
                    if (state.canRenameGroup && state.isGroupNameChanged) {
                        renameGroupUseCase(state.groupName)
                    } else {
                        Result.success(Unit)
                    }
                }
            ).firstOrNull { result -> result.isFailure }
                ?.exceptionOrNull()
                ?.let { throwable ->
                    _uiEvent.emit(EditProfileUiEvent.UpdateFailure(throwable))
                }
                ?: _uiEvent.emit(EditProfileUiEvent.UpdateSuccess)
        }
    }
}

data class Profile(
    val avatar: Avatar,
    val displayName: String,
    val groupName: String,
) {
    val isValid: Boolean
        get() = displayName.isNotBlank() && groupName.isNotBlank()
}

sealed class EditProfileUiState {
    data object Loading : EditProfileUiState()

    data class Success(
        private val original: Profile,
        val draft: Profile,
        val canRenameGroup: Boolean,
    ) : EditProfileUiState() {
        val avatar: Avatar
            get() = draft.avatar

        val displayName: String
            get() = draft.displayName

        val groupName: String
            get() = draft.groupName

        val isUserProfileChanged: Boolean
            get() = original.avatar != draft.avatar ||
                    original.displayName != draft.displayName

        val isGroupNameChanged: Boolean
            get() = original.groupName != draft.groupName

        val hasChanges: Boolean
            get() = isUserProfileChanged || isGroupNameChanged

        val canComplete: Boolean
            get() = hasChanges && draft.isValid
    }

    data class Failure(val error: Throwable) : EditProfileUiState()
}