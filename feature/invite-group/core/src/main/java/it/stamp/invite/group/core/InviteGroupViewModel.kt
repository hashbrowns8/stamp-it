package it.stamp.invite.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.GetInviteCodeUseCase
import it.stamp.model.ids.GroupId
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteGroupViewModel @Inject constructor(
    private val getInviteCodeUseCase: GetInviteCodeUseCase,
) : ViewModel() {

    private val _inviteCode = MutableStateFlow<InviteCode?>(null)
    val inviteCode: StateFlow<InviteCode?> = _inviteCode.asStateFlow()

    private val _uiEvent = MutableSharedFlow<InviteGroupUiEvent>()
    val uiEvent: SharedFlow<InviteGroupUiEvent> = _uiEvent.asSharedFlow()

    fun getInviteCode(groupId: GroupId) {
        viewModelScope.launch {
            getInviteCodeUseCase(groupId)
                .onSuccess {
                    _inviteCode.value = it
                }
                .onFailure { throwable ->
                    _uiEvent.emit(InviteGroupUiEvent.GetInviteCodeFailed(throwable))
                }
        }
    }
}