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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.icon.Check
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.X
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun MemberMissionListItem(
    mission: Mission,
    assignee: Member,
    modifier: Modifier = Modifier,
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
            val dateFormat = remember {
                LocalDate.Format {
                    monthNumber(Padding.NONE); char('/'); day(Padding.NONE)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Chip("to.${assignee.displayName.value}")
                Chip("~${dateFormat.format(dueDate)}")
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
            val text = remember(dueDate) {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

                when (val days = (dueDate - today).days) {
                    0 -> "오늘"
                    1 -> "내일"
                    in 2..Int.MAX_VALUE -> "${days}일 전"
                    else -> ""
                }
            }

            Text(
                text,
                color = Gray500,
                style = MaterialTheme.typography.bodySmall,
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val contentColor = if (isOverdue) {
                    Gray400
                } else if (isCompleted) {
                    Red400
                } else {
                    Color.Unspecified
                }

                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    when {
                        isOverdue -> {
                            Icon(
                                imageVector = Drawables.X,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )

                            Text("만료", style = MaterialTheme.typography.labelSmall)
                        }
                        isCompleted -> {
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