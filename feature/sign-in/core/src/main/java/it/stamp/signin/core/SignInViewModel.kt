package it.stamp.signin.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<SignInUiEvent>()
    val uiEvent: SharedFlow<SignInUiEvent> = _uiEvent.asSharedFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            signInWithGoogleUseCase(idToken)
                .onSuccess { user ->
                    _uiEvent.emit(SignInUiEvent.SignedIn(user))
                }
                .onFailure { throwable ->
                    _uiEvent.emit(SignInUiEvent.SignInFailed(throwable))
                }
        }
    }
}