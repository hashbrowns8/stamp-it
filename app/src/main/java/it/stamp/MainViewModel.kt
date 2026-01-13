package it.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.user.ObserveAuthenticationStateUseCase
import it.stamp.model.authentication.AuthState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthenticationStateUseCase: ObserveAuthenticationStateUseCase,
) : ViewModel() {

    val authState: StateFlow<AuthState> = observeAuthenticationStateUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AuthState.Unknown)
}