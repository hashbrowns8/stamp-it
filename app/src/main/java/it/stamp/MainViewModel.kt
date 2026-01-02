package it.stamp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.ObserveCurrentUserUseCase
import it.stamp.model.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeCurrentUserUseCase: ObserveCurrentUserUseCase,
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = observeCurrentUserUseCase()
        .map<User?, MainUiState> { user ->
            MainUiState.Success(user)
        }
        .catch { throwable ->
            emit(MainUiState.Failure(throwable))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainUiState.Loading)
}