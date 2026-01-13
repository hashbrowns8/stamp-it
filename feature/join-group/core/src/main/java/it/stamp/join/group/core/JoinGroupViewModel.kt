package it.stamp.join.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.JoinGroup
import it.stamp.domain.usecase.group.JoinGroupWithInviteCodeUseCase
import it.stamp.domain.usecase.group.TransferGroupUseCase
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel @Inject constructor(
    private val joinGroupWithInviteCode: JoinGroupWithInviteCodeUseCase,
    private val transferGroup: TransferGroupUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(JoinGroupUiState())
    val uiState: StateFlow<JoinGroupUiState> = _uiState.asStateFlow()

    fun updateInviteCode(inviteCode: String) {
        _uiState.value = JoinGroupUiState(inviteCode, inProgress = false)
    }

    private val _uiEvent = MutableSharedFlow<JoinGroupUiEvent>()
    val uiEvent: SharedFlow<JoinGroupUiEvent> = _uiEvent.asSharedFlow()

    fun joinGroup(inviteCode: InviteCode) {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(inProgress = true)
            }

            joinGroupWithInviteCode(inviteCode)
                .onSuccess { result ->
                    when (result) {
                        JoinGroup.InvalidCode -> JoinGroupUiEvent.InvalidCode
                        JoinGroup.AlreadyInGroup -> JoinGroupUiEvent.AlreadyInGroup

                        is JoinGroup.RequiresDataLossConsent -> with(result) {
                            JoinGroupUiEvent.RequestDataLossConsent(leavingGroup, joiningGroup)
                        }

                        is JoinGroup.Success -> JoinGroupUiEvent.JoinGroupSuccess(result.group)
                    }.let { value ->
                        _uiEvent.emit(value)
                    }
                }
                .onFailure { throwable ->
                    JoinGroupUiEvent.JoinGroupFailure(throwable)
                }

            _uiState.update { uiState ->
                uiState.copy(inProgress = false)
            }
        }
    }

    fun acceptDataLoss(leavingGroup: Group, joiningGroup: Group) {
        viewModelScope.launch {
            transferGroup(leavingGroup, joiningGroup)
                .map { group ->
                    JoinGroupUiEvent.JoinGroupSuccess(group)
                }
                .getOrElse { throwable ->
                    JoinGroupUiEvent.JoinGroupFailure(throwable)
                }
                .let {
                    _uiEvent.emit(it)
                }
        }
    }
}

data class JoinGroupUiState(
    val inviteCode: String = String(),
    val inProgress: Boolean = false,
)