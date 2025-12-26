package it.stamp.home

import it.stamp.model.mission.Mission

sealed interface HomeUiEvent {
    data class MissionCompleted(val mission: Mission) : HomeUiEvent
    data class MissionCompletionCanceled(val mission: Mission) : HomeUiEvent
    data class OperationFailed(val throwable: Throwable) : HomeUiEvent
}