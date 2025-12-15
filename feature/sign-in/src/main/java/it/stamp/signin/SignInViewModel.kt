package it.stamp.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.BootstrapNewUserUseCase
import it.stamp.domain.usecase.SignInWithGoogleUseCase
import it.stamp.model.authentication.AuthenticationResult
import it.stamp.model.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val bootstrapNewUserUseCase: BootstrapNewUserUseCase,
) : ViewModel() {

    private val uiState = MutableStateFlow<SignInUiState>(SignInUiState.Nothing)

    val kUiState: StateFlow<SignInUiState> = uiState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            when (val result = signInWithGoogleUseCase(idToken)) {
                is AuthenticationResult.Authenticated -> with(result) {
                    if (isNewUser) {
                        bootstrapNewUserUseCase(user)
                            .onSuccess { user ->
                                uiState.value = SignInUiState.Authenticated(user, isNewUser = true)
                            }
                            .onFailure {
                                uiState.value = SignInUiState.Failure(errorMessage = "로그인에 실패하였습니다. :-/") // TODO
                            }
                    } else {
                        uiState.value = SignInUiState.Authenticated(user, isNewUser = false)
                    }
                }
                is AuthenticationResult.Failure -> with(result) {
                    uiState.value = SignInUiState.Failure(errorMessage = "로그인에 실패하였습니다. 다시 시도해주세요.")
                }
            }
        }
    }
}

sealed interface SignInUiState {
    data object Nothing : SignInUiState
    data class Authenticated(val user: User, val isNewUser: Boolean) : SignInUiState
    data class Failure(val errorMessage: String) : SignInUiState
}