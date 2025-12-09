package it.stamp.model.mission

enum class MissionStatus {
    ASSIGNED,
    COMPLETED,
    FAILED;

    val isInProgress: Boolean
        get() = this == ASSIGNED
}