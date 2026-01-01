package it.stamp.home.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.ButtonSize
import it.stamp.designsystem.component.SecondaryButton
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.core.R

@Composable
fun EmptyMissionView(
    description: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Gray50, RoundedCornerShape(8.dp))
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = description,
            color = Gray800,
            style = MaterialTheme.typography.bodySmall,
        )

        SecondaryButton(
            onClick = onActionClick,
            size = ButtonSize.Small,
        ) {
            Text(actionLabel)
        }
    }
}

@Preview
@Composable
private fun EmptyMissionViewPreview() {
    StampTheme {
        EmptyMissionView(
            stringResource(R.string.no_mission_assigned_to_me),
            stringResource(R.string.request_new_mission),
            onActionClick = {
            },
            modifier = Modifier
                .background(White)
                .padding(24.dp)
        )
    }
}