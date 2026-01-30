package it.stamp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.Red50
import it.stamp.designsystem.theme.White

@Composable
fun SelectableButton(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    unselectedBorderColor: Color = Gray50,
    selectedBorderColor: Color = Red400,
    unselectedBackgroundColor: Color = White,
    selectedBackgroundColor: Color = Red50,
) {
    val borderColor = if (selected) {
        selectedBorderColor
    } else {
        unselectedBorderColor
    }

    val backgroundColor = if (selected) {
        selectedBackgroundColor
    } else {
        unselectedBackgroundColor
    }

    val contentPadding = if (description == null) {
        PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    } else {
        PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically),
    ) {
        Text(
            text = label,
            color = Gray800,
            style = MaterialTheme.typography.labelMedium,
        )

        if (description != null) {
            Text(
                text = description,
                color = Gray500,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}