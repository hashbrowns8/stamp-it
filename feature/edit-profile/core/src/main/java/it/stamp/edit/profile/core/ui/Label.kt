package it.stamp.edit.profile.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.theme.Gray400

@Composable
internal fun Label(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        modifier,
        color = Gray400,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        style = MaterialTheme.typography.labelSmall,
    )
}