package it.stamp.mypage.core.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800

@Composable
fun MenuItem(
    label: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            label,
            color = Gray800,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.labelMedium,
        )

        Text(
            description,
            color = Gray500,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}