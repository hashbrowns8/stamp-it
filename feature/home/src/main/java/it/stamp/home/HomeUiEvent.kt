package it.stamp.home

import it.stamp.model.mission.Mission

sealed interface HomeUiEvent {
    data class MissionCompleted(val mission: Mission) : HomeUiEvent
    data class MissionCanceled(val mission: Mission) : HomeUiEvent
    data class OperationFailed(val message: String) : HomeUiEvent
}