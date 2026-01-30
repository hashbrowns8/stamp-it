package it.stamp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.model.ids.MissionId
import it.stamp.model.ids.UserId
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

data class MemberMissionUiModel(
    val id: MissionId,
    val category: MissionCategory,
    val title: String,
    val assigneeId: UserId,
    val assigneeDisplayName: String,
    val dueDate: LocalDate,
    val daysAgo: String,
    val status: MissionStatus,
    val isOverdue: Boolean,
    val isDone: Boolean,
)

@Composable
fun MemberMissionListItem(
    mission: MemberMissionUiModel,
    modifier: Modifier = Modifier,
    dateFormat: DateTimeFormat<LocalDate> = remember {
        LocalDate.Format {
            monthNumber(Padding.NONE); char('/'); day(Padding.NONE)
        }
    }
) = with(mission) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(category.backgroundColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Image(
                painter = category.image,
                contentDescription = null,
            )
        }

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Chip(stringResource(R.string.to_assignee, assigneeDisplayName))
                Chip(stringResource(R.string.due_date, dateFormat.format(dueDate)))
            }

            Text(
                text = title,
                color = Gray800,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = daysAgo,
                color = Gray500,
                style = MaterialTheme.typography.bodySmall,
            )

            val contentColor = if (isOverdue) {
                Gray400
            } else if (isDone) {
                Red400
            } else {
                Color.Unspecified
            }

            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when {
                        isOverdue -> {
                            Icon(
                                imageVector = Drawables.X,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )

                            Text("만료", style = MaterialTheme.typography.labelSmall)
                        }

                        isDone -> {
                            Icon(
                                imageVector = Drawables.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )

                            Text("완료", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

// TODO
@Composable
private fun Chip(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Gray25,
    textColor: Color = Gray400,
    shape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(paddingValues),
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 1.em,
        )
    }
}