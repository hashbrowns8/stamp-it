package it.stamp.invite.member.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.group.GetInviteCode
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val getInviteCode: GetInviteCode,
) : ViewModel() {
    val inviteCode: StateFlow<InviteCode?> = flow {
        getInviteCode()
            .onSuccess { inviteCode ->
                emit(inviteCode)
            }
            .onFailure { // TODO
                emit(null)
            }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}