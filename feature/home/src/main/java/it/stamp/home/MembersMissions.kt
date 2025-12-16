package it.stamp.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.White
import it.stamp.model.ids.MissionId
import it.stamp.model.mission.MissionCategory
import it.stamp.model.mission.MissionStatus
import kotlinx.datetime.LocalDate

@Composable
fun MembersMissions(modifier: Modifier = Modifier) {

}

data class MemberMission(
    val id: MissionId,
    val category: MissionCategory,
    val title: String,
    val dueDate: LocalDate,
    val assigneeName: String,
    val status: MissionStatus,
)

@Composable
fun MemberFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    displayName: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(
        containerColor = White,
        labelColor = Gray400,
        selectedContainerColor = Red400,
        selectedLabelColor = White,
    ),
    border: BorderStroke = FilterChipDefaults.filterChipBorder(
        enabled,
        selected,
        borderColor = Gray200,
        borderWidth = 1.dp,
    )
) {
    FilterChip(
        selected,
        onClick,
        label = {
            Text(displayName, style = MaterialTheme.typography.labelSmall)
        },
        modifier,
        enabled,
        shape = shape,
        colors = colors,
        elevation = null,
        border = border,
    )
}