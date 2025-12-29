package it.stamp.designsystem.theme

import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun StampTheme(
    typography: Typography = Typography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        MaterialTheme(typography = typography, content = content)
    }
}