package it.stamp.mypage.core.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.ObserveMyGroupUseCase
import it.stamp.domain.usecase.membership.LeaveGroupUseCase
import it.stamp.domain.usecase.user.DeleteAccountUseCase
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.domain.usecase.user.SignOutUseCase
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyGroupUseCase: ObserveMyGroupUseCase,
    private val leaveGroupUseCase: LeaveGroupUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    val uiState: StateFlow<MyProfileUiState> = observeCurrentUserUseCase().filterNotNull()
        .combine(observeMyGroupUseCase().filterNotNull()) { user, group ->
            MyProfileUiState.Success(user, group)
        }.catch { throwable ->
            MyProfileUiState.Failure(throwable)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, MyProfileUiState.Loading)

    fun leaveGroup() {
        viewModelScope.launch {
            leaveGroupUseCase()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            deleteAccountUseCase()
        }
    }
}

sealed interface MyProfileUiState {
    data object Loading : MyProfileUiState

    data class Success(val user: User, val group: Group) : MyProfileUiState

    data class Failure(val throwable: Throwable) : MyProfileUiState
}