package it.stamp.edit.profile.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.exception.GroupNotFoundException
import it.stamp.domain.exception.MembershipNotFoundException
import it.stamp.domain.exception.NotAuthenticatedException
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.domain.usecase.membership.ObserveMyMembershipUseCase
import it.stamp.domain.usecase.user.EditProfileCommand
import it.stamp.domain.usecase.user.EditProfileUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.model.membership.Group
import it.stamp.model.membership.Membership
import it.stamp.model.user.Avatar
import it.stamp.model.user.DisplayName
import it.stamp.model.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyMembershipUseCase: ObserveMyMembershipUseCase,
    observeMyGroupUseCase: ObserveMyGroupUseCase,
    private val editProfileUseCase: EditProfileUseCase,
) : ViewModel() {

    private val user = observeCurrentUserUseCase()
        .map { user ->
            user ?: throw NotAuthenticatedException()
        }

    private val membership = observeMyMembershipUseCase()
        .map { membership ->
            membership ?: throw MembershipNotFoundException()
        }

    private val group = observeMyGroupUseCase()
        .map { group ->
            group ?: throw GroupNotFoundException()
        }

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Loading)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(user, membership, group) { user, membership, group ->
                EditProfileUiState.Success(user, membership, group) as EditProfileUiState
            }.catch { throwable ->
                emit(EditProfileUiState.Failure(throwable))
            }.collect(_uiState)
        }
    }

    private inline fun update(block: (EditProfileUiState.Success) -> EditProfileUiState.Success) {
        _uiState.update { uiState ->
            if (uiState is EditProfileUiState.Success) {
                block(uiState)
            } else {
                uiState
            }
        }
    }

    fun updateAvatar(avatar: Avatar) = update { uiState ->
        uiState.copy(avatar = avatar)
    }

    fun updateDisplayName(displayName: String) = update { uiState ->
        uiState.copy(displayName = displayName)
    }

    fun updateGroupName(groupName: String) = update { uiState ->
        uiState.copy(groupName = groupName)
    }

    fun complete() {
        viewModelScope.launch {
            val uiState = uiState.value

            if (uiState !is EditProfileUiState.Success || !uiState.isModified) return@launch

            val (user, group) = with(uiState) {
                Pair(
                    if (isUserProfileModified) {
                        user.copy(
                            avatar = avatar,
                            displayName = DisplayName(displayName)
                        )
                    } else {
                        null
                    },
                    if (isGroupNameModified) {
                        group.copy(name = groupName)
                    } else {
                        null
                    }
                )
            }

            editProfileUseCase(EditProfileCommand(user, group))
                .onFailure {
                    Timber.d(it)
                }
        }
    }
}

sealed interface EditProfileUiState {
    data object Loading : EditProfileUiState

    data class Success(
        val user: User,
        val membership: Membership,
        val group: Group,
        val avatar: Avatar = user.avatar,
        val displayName: String = user.displayName.value,
        val groupName: String = group.name,
    ) : EditProfileUiState {
        val canEditGroupName: Boolean
            get() = membership.isLeader

        val isUserProfileModified: Boolean
            get() = user.avatar != avatar
                    || user.displayName.value != displayName

        val isGroupNameModified: Boolean
            get() = group.name != groupName

        val isModified: Boolean
            get() = isUserProfileModified || isGroupNameModified

        val canComplete: Boolean
            get() = isModified // TODO : check displayName, groupName isValid
    }

    data class Failure(val throwable: Throwable) : EditProfileUiState
}