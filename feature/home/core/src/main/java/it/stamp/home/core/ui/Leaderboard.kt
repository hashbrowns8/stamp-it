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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import it.stamp.designsystem.icon.CharacterRed
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.FirstRank
import it.stamp.designsystem.icon.SecondRank
import it.stamp.designsystem.icon.ThirdRank
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.home.core.R
import it.stamp.home.core.preview.SampleMe
import it.stamp.home.core.preview.SampleRankings
import it.stamp.model.membership.Member
import it.stamp.model.stamp.LeaderboardMember

@Composable
fun Leaderboard(
    user: Member,
    rankings: List<LeaderboardMember>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(rankings) { member ->
                Item(
                    member,
                    isMe = member.member.id == user.id,
                )
            }
        }
    }
}

@Composable
private fun Item(
    member: LeaderboardMember,
    isMe: Boolean,
    modifier: Modifier = Modifier,
) = with(member) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(64.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, Gray200, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    Drawables.CharacterRed, // TODO
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                )
            }

            if (member.rank in 1..3) {
                val imageVector = when (member.rank) {
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
                this@with.member.displayName
            },
            modifier = Modifier.widthIn(max = 60.dp),
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
        Leaderboard(SampleMe, SampleRankings)
    }
}