package it.stamp.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.stamp.domain.usecase.GetGroupLeaderboardUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGroupLeaderboardUseCase: GetGroupLeaderboardUseCase,
) : ViewModel() {

}

data class HomeUiState(
    val isLoading: Boolean = false,

)