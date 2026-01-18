package it.stamp.home.core.ui

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.core.R
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.ui.MemberFilterChip
import it.stamp.ui.MemberMissionListItem
import it.stamp.ui.PreviewSamples

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
                    displayName = member.displayName.value,
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

@Preview
@Composable
private fun MembersMissionsPreview() {
    StampTheme {
        with(PreviewSamples) {
            MembersMissions(
                userDisplayName = Me.displayName.value,
                groupName = Group.name,
                members = Members - MeAsMember,
                memberMissions = MemberMissions,
                onAssignNewMissionClick = {
                },
                modifier = Modifier.background(White)
            )
        }
    }
}

@Preview
@Composable
private fun MembersMissionsEmptyPreview() {
    StampTheme {
        with(PreviewSamples) {
            MembersMissions(
                userDisplayName = Me.displayName.value,
                groupName = Group.name,
                members = Members - MeAsMember,
                memberMissions = emptyList(),
                onAssignNewMissionClick = {
                },
                modifier = Modifier.background(White)
            )
        }
    }
}