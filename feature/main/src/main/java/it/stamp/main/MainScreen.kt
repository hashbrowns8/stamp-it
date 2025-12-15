package it.stamp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.stamp.designsystem.theme.Red50

@Composable
internal fun MainScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(Red50))
}