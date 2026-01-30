package it.stamp.membership.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray100
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.membership.core.R
import it.stamp.model.membership.Member
import it.stamp.ui.AvatarImage
import it.stamp.ui.PreviewSamples
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

private val DateFormat = LocalDate.Format {
    year(); char('년')

    char(' ')

    monthNumber(Padding.NONE); char('월')

    char(' ')

    day(Padding.NONE); char('일')
}

@Composable
fun MemberListItem(
    member: Member,
    canManageMember: Boolean,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    timeZone: TimeZone = remember {
        TimeZone.currentSystemDefault()
    },
    dateFormat: DateTimeFormat<LocalDate> = remember {
        DateFormat
    },
) = with(member) {
    Row(
        modifier = modifier
            .border(1.dp, Gray25, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(1.dp, Gray50, CircleShape)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            AvatarImage(avatar)
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isLeader) {
                    Box(
                        modifier = Modifier
                            .background(Gray25, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "그룹 리더",
                            color = Gray800,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 1.em,
                            style = MaterialTheme.typography.bodyExtraSmall,
                        )
                    }
                }

                Text(
                    text = displayName.value,
                    color = Gray800,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            val memberSince: LocalDate = remember(joinedAt, timeZone) {
                joinedAt
                    .toLocalDateTime(timeZone)
                    .date
            }

            Text(
                text = stringResource(R.string.member_since, dateFormat.format(memberSince)),
                color = Gray500,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        if (canManageMember) {
            Icon(
                Drawables.Dots,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onMenuClick),
                tint = Gray100,
            )
        }
    }
}

@Preview
@Composable
private fun MemberListItemPreview() {
    StampTheme {
        Column(
            modifier = Modifier
                .background(White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val members = PreviewSamples.members

            members.forEach { member ->
                MemberListItem(
                    member,
                    canManageMember = false,
                    onMenuClick = {
                    },
                )
            }
        }
    }
}