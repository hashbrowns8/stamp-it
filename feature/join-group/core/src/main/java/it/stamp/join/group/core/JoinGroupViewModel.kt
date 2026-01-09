package it.stamp.join.group.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.ValidateGroupJoinUseCase
import it.stamp.model.membership.InviteCode
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinGroupViewModel @Inject constructor(
    private val validateGroupJoinUseCase: ValidateGroupJoinUseCase,
) : ViewModel() {

    fun joinGroup(inviteCode: InviteCode) {
        viewModelScope.launch {

        }
    }
}