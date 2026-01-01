package it.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.ObserveAuthenticationStateUseCase
import it.stamp.domain.usecase.ObserveMembershipByUserUseCase
import it.stamp.model.authentication.AuthenticationState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthenticationStateUseCase: ObserveAuthenticationStateUseCase,
    observeMembershipByUserUseCase: ObserveMembershipByUserUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = observeAuthenticationStateUseCase()
        .flatMapLatest { state ->
            (state as? AuthenticationState.Authenticated)
                ?.user
                ?.let { user ->
                    observeMembershipByUserUseCase(user.id)
                        .map { membership ->
                            MainUiState.Success(user, membership) as MainUiState
                        }
                }
                ?: flowOf(MainUiState.Success() as MainUiState)
        }
        .catch { throwable ->
            emit(MainUiState.Failure(throwable))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainUiState.Loading)
}