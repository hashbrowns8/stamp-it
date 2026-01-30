package it.stamp.model.mission

enum class MissionStatus {
    ASSIGNED,
    DONE,
    FAILED;

    val isInProgress: Boolean
        get() = this == ASSIGNED
}