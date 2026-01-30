package it.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.user.ObserveAuthenticationState
import it.stamp.model.authentication.AuthenticationState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthenticationState: ObserveAuthenticationState,
) : ViewModel() {

    val authenticationState: StateFlow<AuthenticationState> = observeAuthenticationState()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AuthenticationState.Unknown)
}