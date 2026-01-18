package it.stamp.home.core

import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalResources
import it.stamp.designsystem.component.displayMissionCompletion
import it.stamp.domain.exception.MissionException
import it.stamp.model.mission.Mission
import it.stamp.ui.LocalSnackbarHostState

sealed interface HomeUiEvent {
    data class MissionCompleted(val mission: Mission) : HomeUiEvent
    data class MissionCompletionCanceled(val mission: Mission) : HomeUiEvent
    data class UpdateMissionStatusFailed(val exception: MissionException) : HomeUiEvent
    data class OperationFailed(val throwable: Throwable) : HomeUiEvent
}

@Composable
fun HomeEventHandler(viewModel: HomeViewModel) {
    val snackbarHostState = LocalSnackbarHostState.current

    val resources = LocalResources.current

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is HomeUiEvent.MissionCompleted -> {
                    val mission = uiEvent.mission
                    val message = resources.getString(R.string.mission_completed)
                    val actionLabel = resources.getString(R.string.cancel)

                    val result = snackbarHostState.displayMissionCompletion(mission.title, message, actionLabel)

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.cancelMissionCompletion(mission.id)
                    }
                }
                is HomeUiEvent.MissionCompletionCanceled -> {}
                is HomeUiEvent.UpdateMissionStatusFailed -> {}
                is HomeUiEvent.OperationFailed -> {}
            }
        }
    }
}