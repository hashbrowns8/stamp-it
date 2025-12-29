package it.stamp.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray600
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.home.R
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.MissionCategory
import it.stamp.ui.backgroundColor
import it.stamp.ui.image
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

data class MyMission(
    val id: MissionId,
    val category: MissionCategory,
    val title: String,
    val dueDate: LocalDate,
    val assignerName: String,
)

@Composable
fun MyMissions(
    userDisplayName: String,
    onViewMoreClick: () -> Unit,
    missions: List<MyMission>,
    onRequestNewMissionClick: () -> Unit,
    onMissionCompleteClick: (MissionId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        SectionHeader(
            title = stringResource(R.string.my_missions_title),
            description = stringResource(R.string.my_missions_description, userDisplayName), // TODO
            onViewMoreClick,
            modifier = Modifier.padding(16.dp),
        )

        if (missions.isEmpty()) {
            EmptyMissionView(
                stringResource(R.string.no_mission_assigned_to_me),
                stringResource(R.string.request_new_mission),
                onRequestNewMissionClick,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(Modifier)

                missions.forEach { mission ->
                    MyMissionCard(
                        mission,
                        onCompleteClick = onMissionCompleteClick,
                        modifier = Modifier.fillMaxHeight(),
                    )
                }

                Spacer(Modifier)
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun MyMissionCard(
    mission: MyMission,
    onCompleteClick: (MissionId) -> Unit,
    modifier: Modifier = Modifier,
) = with(mission) {
    Column(
        modifier = modifier
            .width(200.dp)
            .background(category.backgroundColor, RoundedCornerShape(20.dp))
            .padding(start = 24.dp, top = 20.dp, end = 24.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = category.image,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .background(Gray25, CircleShape)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${dueDate.month.number}/${dueDate.day}",
                    color = Gray800,
                    style = MaterialTheme.typography.bodyExtraSmall,
                )
            }

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .background(Gray25, CircleShape)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = assignerName,
                    color = Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyExtraSmall,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                color = Gray800,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(White)
                .clickable {
                    onCompleteClick(id)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.complete_mission),
                color = Gray600,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MyMissionsEmptyPreview() {
    StampTheme {
        MyMissions(
            userDisplayName = "즐거운 호랑이-4325df4",
            onViewMoreClick = {},
            missions = emptyList(),
            onRequestNewMissionClick = {},
            onMissionCompleteClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyMissionsPreview() {
    StampTheme {
        val myMissions = remember {
            listOf(
                MyMission(
                    id = MissionId("1"),
                    category = MissionCategory.CHORE,
                    title = "이불 빨고 말리기",
                    dueDate = LocalDate(2023, 12, 15),
                    assignerName = "즐거운 호랑이-AOSTEST",
                ),
                MyMission(
                    id = MissionId("2"),
                    category = MissionCategory.HEALTH,
                    title = "건강한 수면 환경 함께 조성하기",
                    dueDate = LocalDate(2023, 12, 18),
                    assignerName = "고나리",
                ),
            )
        }

        MyMissions(
            userDisplayName = "즐거운 호랑이-4325df4",
            onViewMoreClick = {},
            missions = myMissions,
            onRequestNewMissionClick = {},
            onMissionCompleteClick = {},
        )
    }
}