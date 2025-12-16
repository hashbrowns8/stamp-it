package it.stamp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.FirstRank
import it.stamp.designsystem.icon.Icons
import it.stamp.designsystem.icon.SecondRank
import it.stamp.designsystem.icon.ThirdRank
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.model.ids.UserId

data class LeaderboardMember(
    val id: UserId,
    val displayName: String,
    val avatar: String?,
    val rank: Int,
    val stamps: Int,
)

@Composable
fun Leaderboard(
    members: List<LeaderboardMember>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(members) { member ->
                Item(member)
            }
        }
    }
}

@Composable
private fun Item(
    member: LeaderboardMember,
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
                    .background(Gray25) // TODO
                    .border(1.dp, Gray200, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                // TODO
            }

            if (member.rank in 1..3) {
                val imageVector = when (member.rank) {
                    1 -> Icons.FirstRank
                    2 -> Icons.SecondRank
                    else -> Icons.ThirdRank
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
            color = Black,
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
    StampItTheme {
        val members = remember {
            listOf(
                LeaderboardMember(
                    id = UserId("1"),
                    displayName = "엄마",
                    avatar = null,
                    rank = 1,
                    stamps = 22,
                ),
                LeaderboardMember(
                    id = UserId("2"),
                    displayName = "유진",
                    avatar = null,
                    rank = 2,
                    stamps = 20,
                ),
                LeaderboardMember(
                    id = UserId("3"),
                    displayName = "파덜",
                    avatar = null,
                    rank = 3,
                    stamps = 12,
                ),
                LeaderboardMember(
                    id = UserId("4"),
                    displayName = "나",
                    avatar = null,
                    rank = 4,
                    stamps = 8,
                ),
            )
        }

        Leaderboard(members)
    }
}