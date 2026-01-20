package it.stamp.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.stamp.designsystem.icon.Drawables

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick, modifier) {
        Icon(
            imageVector = Drawables.ArrowLeft,
            contentDescription = null,
        )
    }
}