package it.stamp.membership.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.exception.MemberRemovalException
import it.stamp.domain.usecase.leadership.RemoveMemberFromGroupUseCase
import it.stamp.domain.usecase.leadership.TransferLeadershipUseCase
import it.stamp.domain.usecase.membership.ObserveMyGroupMembers
import it.stamp.domain.usecase.user.ObserveCurrentUserUseCase
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import it.stamp.model.user.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MembershipViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    observeMyGroupMembers: ObserveMyGroupMembers,
    private val transferLeadershipUseCase: TransferLeadershipUseCase,
    private val removeMemberFromGroupUseCase: RemoveMemberFromGroupUseCase,
) : ViewModel() {

    val uiState: StateFlow<MembershipUiState> = observeCurrentUserUseCase().filterNotNull()
        .combine(observeMyGroupMembers()) { user, members ->
            val canManageMember = members
                .first { it.id == user.id }
                .isLeader

            MembershipUiState.Success(user, members, canManageMember)
        }
        .catch { throwable ->
            MembershipUiState.Failure(throwable)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MembershipUiState.Loading)

    private val _uiEvent = MutableSharedFlow<MembershipUiEvent>()
    val uiEvent: SharedFlow<MembershipUiEvent> = _uiEvent.asSharedFlow()

    fun transferLeadership(memberId: UserId) {
        viewModelScope.launch {
            transferLeadershipUseCase(memberId)
                .onSuccess {
                    _uiEvent.emit(MembershipUiEvent.LeadershipTransferred)
                }
                .onFailure { throwable ->
                    _uiEvent.emit(MembershipUiEvent.MemberRemovalFailed(throwable))
                }
        }
    }

    fun removeMember(memberId: UserId) {
        viewModelScope.launch {
            removeMemberFromGroupUseCase(memberId)
                .map {
                    MembershipUiEvent.MemberRemoved(memberId)
                }
                .getOrElse { throwable ->
                    if (throwable is MemberRemovalException) {
                        when (throwable) {
                            is MemberRemovalException.MemberNotFound -> MembershipUiEvent.MemberRemoved(memberId)
                        }
                    } else {
                        MembershipUiEvent.MemberRemovalFailed(throwable)
                    }
                }
                .let { event ->
                    _uiEvent.emit(event)
                }
        }
    }
}

sealed interface MembershipUiState {
    data object Loading : MembershipUiState

    data class Success(
        val user: User,
        val members: List<Member>,
        val canManageMember: Boolean,
    ) : MembershipUiState

    data class Failure(val throwable: Throwable) : MembershipUiState
}