package it.stamp.invite.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.GetInviteCodeUseCase
import it.stamp.domain.usecase.ObserveCurrentMembershipUseCase
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InviteGroupViewModel @Inject constructor(
    observeCurrentMembershipUseCase: ObserveCurrentMembershipUseCase,
    private val getInviteCodeUseCase: GetInviteCodeUseCase,
) : ViewModel() {
    // TODO : 오류

    val inviteCode: StateFlow<InviteCode> = observeCurrentMembershipUseCase()
        .catch {
        }
        .filterNotNull()
        .map { membership ->
            getInviteCodeUseCase(membership.groupId)
                .getOrNull()
                ?: InviteCode.Empty
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, InviteCode.Empty)
}