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
import androidx.compose.runtime.remember
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
import it.stamp.model.ids.UserId
import it.stamp.model.user.Avatar
import it.stamp.ui.AvatarImage
import it.stamp.ui.PreviewSamples
import kotlin.random.Random

data class MemberUiModel(
    val id: UserId,
    val avatar: Avatar,
    val displayName: String,
)

data class LeaderboardEntryUiModel(
    val member: MemberUiModel,
    val isMe: Boolean,
    val rank: Int,
    val stampCount: Int,
)

@Composable
fun Leaderboard(
    rankings: List<LeaderboardEntryUiModel>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = rankings,
                key = { entry ->
                    entry.member.id.value
                },
            ) { entry ->
                with(entry) {
                    Entry(
                        member.avatar,
                        rank,
                        displayName = if (isMe) {
                            stringResource(R.string.me)
                        } else {
                            member.displayName
                        },
                        stampCount,
                    )
                }
            }
        }
    }
}

@Composable
private fun Entry(
    avatar: Avatar,
    rank: Int,
    displayName: String,
    stampCount: Int,
    modifier: Modifier = Modifier,
) {
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
                    avatar,
                    modifier = Modifier.size(40.dp),
                )
            }

            if (rank in 1..3) {
                val imageVector = when (rank) {
                    1 -> Drawables.FirstRank
                    2 -> Drawables.SecondRank
                    3 -> Drawables.ThirdRank
                    else -> throw IllegalStateException()
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
            text = displayName,
            modifier = Modifier.widthIn(max = 56.dp),
            color = Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
        )

        Text(
            text = "${stampCount}개",
            color = Gray300,
            style = MaterialTheme.typography.bodyExtraSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaderboardPreview() {
    StampTheme {
        val rankings = remember {
            val members = PreviewSamples.members

            members.mapIndexed { index, member ->
                val member = with(member) {
                    MemberUiModel(
                        id,
                        avatar,
                        displayName = displayName.value,
                    )
                }

                val rank = index + 1

                val seed = members.size - rank

                val stampCount = Random.nextInt(seed * 10, (seed + 1) * 10)

                LeaderboardEntryUiModel(
                    member,
                    isMe = index == 0,
                    rank,
                    stampCount,
                )
            }
        }

        Leaderboard(rankings)
    }
}