package it.stamp.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.R
import it.stamp.home.sampleGroup
import it.stamp.home.sampleMe
import it.stamp.home.sampleMemberMissions
import it.stamp.home.sampleMembers
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.ui.MemberMissionListItem

@Composable
fun MembersMissions(
    userDisplayName: String,
    groupName: String,
    members: List<Member>,
    memberMissions: List<Mission>,
    onAssignNewMissionClick: (assignee: UserId?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        SectionHeader(
            title = stringResource(R.string.members_missions_title),
            description = stringResource(
                R.string.members_mission_description,
                userDisplayName,
                groupName,
            ),
            onViewMoreClick = {
            },
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        )

        var selectedMember: Member? by remember {
            mutableStateOf(null)
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                MemberFilterChip(
                    selected = selectedMember == null,
                    onClick = {
                        selectedMember = null
                    },
                    displayName = stringResource(R.string.all),
                    modifier = Modifier.height(32.dp),
                )
            }

            items(members) { member ->
                MemberFilterChip(
                    selected = selectedMember == member,
                    onClick = {
                        selectedMember = member
                    },
                    displayName = member.displayName,
                    modifier = Modifier.height(32.dp),
                )
            }
        }

        val membersById = remember(members) {
            members.associateBy { it.id }
        }

        val missionsFiltered by remember {
            derivedStateOf {
                if (selectedMember == null) {
                    memberMissions
                } else {
                    memberMissions.filter { mission -> mission.assignee == selectedMember?.id }
                }.take(4) // TODO
            }
        }

        Column {
            if (missionsFiltered.isEmpty()) {
                EmptyMissionView(
                    stringResource(R.string.no_mission_assigned_by_me),
                    stringResource(R.string.assign_new_mission),
                    onActionClick = {
                        onAssignNewMissionClick(selectedMember?.id)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            } else {
                missionsFiltered.forEachIndexed { index, mission ->
                    MemberMissionListItem(
                        mission,
                        assignee = membersById.getValue(mission.assignee),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    if (index != missionsFiltered.lastIndex) {
                        HorizontalDivider(
                            color = Gray50,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

// TODO : `:core:ui`
@Composable
private fun MemberFilterChip(
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

@Preview
@Composable
private fun MembersMissionsPreview() {
    StampItTheme {
        MembersMissions(
            userDisplayName = sampleMe.displayName,
            groupName = sampleGroup.name,
            members = sampleMembers - sampleMe,
            memberMissions = sampleMemberMissions,
            onAssignNewMissionClick = {
            },
            modifier = Modifier.background(White)
        )
    }
}

@Preview
@Composable
private fun MembersMissionsEmptyPreview() {
    StampItTheme {
        MembersMissions(
            userDisplayName = sampleMe.displayName,
            groupName = sampleGroup.name,
            members = sampleMembers - sampleMe,
            memberMissions = emptyList(),
            onAssignNewMissionClick = {
            },
            modifier = Modifier.background(White)
        )
    }
}