package it.stamp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.MissionChore
import it.stamp.designsystem.icon.MissionCommunication
import it.stamp.designsystem.icon.MissionCustom
import it.stamp.designsystem.icon.MissionHealth
import it.stamp.designsystem.icon.MissionLearning
import it.stamp.designsystem.theme.Blue100
import it.stamp.designsystem.theme.Green100
import it.stamp.designsystem.theme.Purple100
import it.stamp.designsystem.theme.Red100
import it.stamp.designsystem.theme.Yellow100
import it.stamp.model.mission.MissionCategory

val MissionCategory.backgroundColor: Color
    get() = when (this) {
        MissionCategory.CHORE -> Red100
        MissionCategory.COMMUNICATION -> Blue100
        MissionCategory.HEALTH -> Yellow100
        MissionCategory.LEARNING -> Purple100
        MissionCategory.CUSTOM -> Green100
    }

val MissionCategory.image: Painter
    @Composable
    get() = when (this) {
        MissionCategory.CHORE -> Drawables.MissionChore
        MissionCategory.COMMUNICATION -> Drawables.MissionCommunication
        MissionCategory.HEALTH -> Drawables.MissionHealth
        MissionCategory.LEARNING -> Drawables.MissionLearning
        MissionCategory.CUSTOM -> Drawables.MissionCustom
    }