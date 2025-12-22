package it.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.ObserveAuthenticationStateUseCase
import it.stamp.domain.usecase.ObserveMembershipByUserUseCase
import it.stamp.model.authentication.AuthenticationState
import it.stamp.model.membership.Membership
import it.stamp.model.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeAuthenticationStateUseCase: ObserveAuthenticationStateUseCase,
    observeMembershipByUserUseCase: ObserveMembershipByUserUseCase,
) : ViewModel() {

    val me: StateFlow<User?> = observeAuthenticationStateUseCase()
        .map {
            (it as? AuthenticationState.Authenticated)?.user
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val membership: StateFlow<Membership?> = me.flatMapLatest { me ->
        me?.id
            ?.let(observeMembershipByUserUseCase::invoke)
            ?: flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}