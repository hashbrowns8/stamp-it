package it.stamp.mypage.core.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyGroupUseCase: ObserveMyGroupUseCase,
) : ViewModel() {

    val uiState: StateFlow<MyProfileUiState> = observeCurrentUserUseCase()
        .combine(observeMyGroupUseCase()) { user, group ->
            if (user == null || group == null) {
                MyProfileUiState.SignOut
            } else {
                MyProfileUiState.Success(user, group)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, MyProfileUiState.Loading)

    fun leaveGroup() {
    }

    fun signOut() {
    }

    fun deleteAccount() {
    }
}

sealed interface MyProfileUiState {
    data object Loading : MyProfileUiState

    data class Success(val user: User, val group: Group) : MyProfileUiState

    data object SignOut : MyProfileUiState
}