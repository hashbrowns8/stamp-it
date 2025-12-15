package it.stamp.home

import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
}

data class HomeUiState(
    val isLoading: Boolean = false,

)