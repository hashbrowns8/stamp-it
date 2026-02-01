package it.stamp.join.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.exception.GroupJoinException
import it.stamp.domain.usecase.membership.JoinGroupUseCase
import it.stamp.domain.usecase.membership.JoinGroupWithInviteCodeUseCase
import it.stamp.join.group.core.JoinGroupUiEvent.DataLossWarning
import it.stamp.model.ids.GroupId
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
    private val joinGroupWithInviteCodeUseCase: JoinGroupWithInviteCodeUseCase,
    private val joinGroupUseCase: JoinGroupUseCase,
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

            joinGroupWithInviteCodeUseCase(inviteCode)
                .map {
                    JoinGroupUiEvent.JoinSuccess
                }
                .getOrElse { throwable ->
                    if (throwable is GroupJoinException) {
                        when (throwable) {
                            is GroupJoinException.AlreadyMember -> JoinGroupUiEvent.AlreadyMember
                            is GroupJoinException.InvalidInviteCode -> JoinGroupUiEvent.InvalidInviteCode
                            is GroupJoinException.RequiresDataLossConsent -> DataLossWarning(throwable.targetGroupId)
                        }
                    } else {
                        JoinGroupUiEvent.JoinFailed(throwable)
                    }
                }
                .let { event ->
                    _uiEvent.emit(event)
                }

            _uiState.update { uiState ->
                uiState.copy(inProgress = false)
            }
        }
    }

    fun acceptDataLoss(targetGroupId: GroupId) {
        viewModelScope.launch {
            joinGroupUseCase(targetGroupId)
                .map { JoinGroupUiEvent.JoinSuccess }
                .getOrElse { throwable ->
                    JoinGroupUiEvent.JoinFailed(throwable)
                }
                .let { event ->
                    _uiEvent.emit(event)
                }
        }
    }
}

data class JoinGroupUiState(
    val inviteCode: String = String(),
    val inProgress: Boolean = false,
)