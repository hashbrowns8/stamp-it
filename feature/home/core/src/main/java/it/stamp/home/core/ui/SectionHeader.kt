package it.stamp.home.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import it.stamp.designsystem.icon.ChevronRight
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.core.R

@Composable
fun SectionHeader(
    title: String,
    description: String,
    onViewMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                modifier = Modifier.weight(1F),
                color = Gray800,
                lineHeight = 1.em,
                style = MaterialTheme.typography.titleLarge,
            )

            CompositionLocalProvider(LocalContentColor provides Gray400) {
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .clickable(onClick = onViewMoreClick)
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.view_all),
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall,
                    )

                    Icon(
                        imageVector = Drawables.ChevronRight,
                        contentDescription = null,
                    )
                }
            }
        }

        Text(
            text = description,
            color = Gray500,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    StampTheme {
        SectionHeader(
            title = "내 미션",
            description = "이번 주 용감한 호랑이님에게 부여된 미션이에요",
            onViewMoreClick = {},
            modifier = Modifier.background(White),
        )
    }
}