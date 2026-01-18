package it.stamp.invite.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.GetInviteCodeUseCase
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InviteGroupViewModel @Inject constructor(
    private val getInviteCodeUseCase: GetInviteCodeUseCase,
) : ViewModel() {
    val inviteCode: StateFlow<InviteCode?> = flow {
        getInviteCodeUseCase()
            .onSuccess { inviteCode ->
                emit(inviteCode)
            }
            .onFailure { // TODO
                emit(null)
            }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}