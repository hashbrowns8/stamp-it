package it.stamp.missions.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.stamp.designsystem.theme.White

@Composable
internal fun MissionsScreen(modifier: Modifier = Modifier) {
    Surface(modifier, color = White) {
        Box(Modifier.fillMaxSize())
    }
}