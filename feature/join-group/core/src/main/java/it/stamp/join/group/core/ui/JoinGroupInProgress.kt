package it.stamp.join.group.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White

@Composable
fun JoinGroupInProgress() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            listOf(
                Drawables.CharacterRed,
                Drawables.CharacterBlue,
                Drawables.CharacterYellow,
                Drawables.CharacterPurple,
            ).forEach { character ->
                Image(
                    character,
                    contentDescription = null,
                    modifier = Modifier.width(32.dp),
                    contentScale = ContentScale.Fit,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "그룹에 입장 중입니다",
            color = Gray800,
            style = MaterialTheme.typography.labelLarge,
        )

        Text(
            "잠시만 기다려주세요",
            color = Gray400,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun JoinGroupInProgressPreview() {
    StampTheme {
        JoinGroupInProgress()
    }
}