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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeAuthenticationStateUseCase: ObserveAuthenticationStateUseCase,
    observeMembershipByUserUseCase: ObserveMembershipByUserUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = observeAuthenticationStateUseCase()
        .flatMapLatest { state ->
            (state as? AuthenticationState.Authenticated)
                ?.user
                ?.let { user ->
                    observeMembershipByUserUseCase(user.id)
                        .map { membership ->
                            MainActivityUiState.Success(user, membership) as MainActivityUiState
                        }
                }
                ?: flowOf(MainActivityUiState.Success() as MainActivityUiState)
        }
        .catch { throwable ->
            val message = if (throwable is IOException) {
                "네트워크 연결에 실패하였습니다"
            } else {
                "오류가 발생하였습니다. 잠시후 다시 시도해주세요."
            }

            emit(MainActivityUiState.Failure(message))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainActivityUiState.Loading)
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(
        val user: User? = null,
        val membership: Membership? = null,
    ) : MainActivityUiState

    data class Failure(val message: String) : MainActivityUiState
}