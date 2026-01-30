package it.stamp.mypage.core.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.ObserveMyGroup
import it.stamp.domain.usecase.user.ObserveCurrentUser
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    observeCurrentUser: ObserveCurrentUser,
    observeMyGroup: ObserveMyGroup,
) : ViewModel() {

    val uiState: StateFlow<MyProfileUiState> = observeCurrentUser()
        .combine(observeMyGroup()) { user, group ->
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