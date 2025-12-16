package it.stamp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.ButtonSize
import it.stamp.designsystem.component.SecondaryButton
import it.stamp.designsystem.icon.MissionChore
import it.stamp.designsystem.icon.MissionCommunication
import it.stamp.designsystem.icon.MissionCustom
import it.stamp.designsystem.icon.MissionHealth
import it.stamp.designsystem.icon.MissionLearning
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Blue100
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray600
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Green100
import it.stamp.designsystem.theme.Purple100
import it.stamp.designsystem.theme.Red100
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White
import it.stamp.designsystem.theme.Yellow100
import it.stamp.designsystem.theme.bodyExtraSmall
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

data class MyMission(
    val id: MissionId,
    val category: MissionCategory,
    val title: String,
    val dueDate: LocalDate,
    val assignerName: String,
    val status: MissionStatus,
)

@Composable
fun MyMissions(
    displayName: String,
    onViewAllClick: () -> Unit,
    missions: List<MyMission>,
    onMissionRequestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(
            title = stringResource(R.string.my_missions_title),
            description = stringResource(R.string.my_missions_description, displayName), // TODO
            onViewAllClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        if (missions.isEmpty()) {
            EmptyMyMissionView(
                onMissionRequestClick,
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
                    MyMissionCard(mission, modifier = Modifier.fillMaxHeight())
                }

                Spacer(Modifier)
            }
        }
    }
}

@Composable
fun EmptyMyMissionView(
    onMissionRequestClick: () -> Unit,
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
            text = "아직 부여된 미션이 없어요!",
            color = Gray800,
            style = MaterialTheme.typography.bodySmall,
        )

        SecondaryButton(
            onClick = onMissionRequestClick,
            size = ButtonSize.Small,
        ) {
            Text("미션 조르기")
        }
    }
}

val MissionCategory.containerColor: Color
    get() = when (this) {
        MissionCategory.CHORE -> Red100
        MissionCategory.COMMUNICATION -> Blue100
        MissionCategory.HEALTH -> Yellow100
        MissionCategory.LEARNING -> Purple100
        MissionCategory.CUSTOM -> Green100
    }

val MissionCategory.image: Painter
    @Composable
    get() = when (this) {
        MissionCategory.CHORE -> MissionChore
        MissionCategory.COMMUNICATION -> MissionCommunication
        MissionCategory.HEALTH -> MissionHealth
        MissionCategory.LEARNING -> MissionLearning
        MissionCategory.CUSTOM -> MissionCustom
    }

@Composable
fun MyMissionCard(
    mission: MyMission,
    modifier: Modifier = Modifier,
) = with(mission) {
    Column(
        modifier = modifier
            .width(200.dp)
            .background(category.containerColor, RoundedCornerShape(20.dp))
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
                .clickable(onClick = {}),
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
    StampItTheme {
        MyMissions(
            displayName = "즐거운 호랑이-4325df4",
            onViewAllClick = {},
            missions = emptyList(),
            onMissionRequestClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyMissionsPreview() {
    StampItTheme {
        val myMissions = remember {
            listOf(
                MyMission(
                    id = MissionId("1"),
                    category = MissionCategory.CHORE,
                    title = "이불 빨고 말리기",
                    dueDate = LocalDate(2023, 12, 15),
                    assignerName = "엄마",
                    status = MissionStatus.ASSIGNED,
                ),
                MyMission(
                    id = MissionId("2"),
                    category = MissionCategory.HEALTH,
                    title = "건강한 수면 환경 함께 조성하기",
                    dueDate = LocalDate(2023, 12, 18),
                    assignerName = "고나리",
                    status = MissionStatus.ASSIGNED,
                ),
            )
        }
        MyMissions(
            displayName = "즐거운 호랑이-4325df4",
            onViewAllClick = {},
            missions = myMissions,
            onMissionRequestClick = {},
        )
    }
}