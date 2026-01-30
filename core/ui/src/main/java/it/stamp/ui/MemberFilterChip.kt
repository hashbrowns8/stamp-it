package it.stamp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.White

@Composable
fun MemberFilterChip(
    selected: Boolean,
    displayName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = White,
    labelColor: Color = Gray400,
    selectedContainerColor: Color = Red400,
    selectedLabelColor: Color = White,
    border: BorderStroke = BorderStroke(1.dp, Gray200),
) {
    val (backgroundColor, labelColor) = if (selected) {
        selectedContainerColor to selectedLabelColor
    } else {
        containerColor to labelColor
    }

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (selected) {
                    Modifier
                } else {
                    Modifier.border(border, shape)
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = displayName,
            color = labelColor,
            lineHeight = 1.em,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}