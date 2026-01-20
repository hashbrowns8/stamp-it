package it.stamp.home.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray100
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.home.core.R
import it.stamp.model.membership.Member
import it.stamp.model.stamp.LeaderboardEntry
import it.stamp.ui.AvatarImage
import it.stamp.ui.PreviewSamples

@Composable
fun Leaderboard(
    user: Member,
    rankings: List<LeaderboardEntry>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(rankings) { entry ->
                Item(
                    entry,
                    isMe = entry.member.id == user.id,
                )
            }
        }
    }
}

@Composable
private fun Item(
    entry: LeaderboardEntry,
    isMe: Boolean,
    modifier: Modifier = Modifier,
) = with(entry) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(1.dp, Gray100, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                AvatarImage(
                    member.avatar,
                    modifier = Modifier.size(40.dp),
                )
            }

            if (entry.rank in 1..3) {
                val imageVector = when (entry.rank) {
                    1 -> Drawables.FirstRank
                    2 -> Drawables.SecondRank
                    else -> Drawables.ThirdRank
                }
                Image(
                    imageVector,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = if (isMe) {
                stringResource(R.string.me)
            } else {
                this@with.member.displayName.value
            },
            modifier = Modifier.widthIn(max = 56.dp),
            color = Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            text = "${stamps}개",
            color = Gray300,
            style = MaterialTheme.typography.bodyExtraSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardPreview() {
    StampTheme {
        Leaderboard(
            PreviewSamples.MeAsMember,
            PreviewSamples.Rankings,
        )
    }
}