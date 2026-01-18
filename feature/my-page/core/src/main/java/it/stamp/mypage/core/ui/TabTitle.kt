package it.stamp.mypage.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray800

@Composable
internal fun TabTitle(
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    selectedContentColor: Color = Gray800,
    unselectedContentColor: Color = Gray200,
    onClick: () -> Unit,
) {
    val color = if (selected) {
        selectedContentColor
    } else {
        unselectedContentColor
    }

    Text(
        text,
        modifier.clickable(onClick = onClick),
        color,
    )
}