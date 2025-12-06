package it.stamp.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun StampItTheme(
    typography: Typography = StampItTypography,
    content: @Composable () -> Unit
) {
    MaterialTheme(typography = typography, content = content)
}