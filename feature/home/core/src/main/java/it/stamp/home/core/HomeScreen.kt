package it.stamp.home.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.component.ActivityIndicator
import it.stamp.designsystem.component.StampTopAppBar
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.core.ui.EmptyGroupView
import it.stamp.home.core.ui.GroupOnboardingModalBottomSheet
import it.stamp.home.core.ui.Leaderboard
import it.stamp.home.core.ui.LeaderboardEntryUiModel
import it.stamp.home.core.ui.MemberUiModel
import it.stamp.home.core.ui.MembersMissions
import it.stamp.home.core.ui.MyMissionUiModel
import it.stamp.home.core.ui.MyMissions
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.model.user.User
import it.stamp.ui.MemberMissionUiModel
import it.stamp.ui.PreviewSamples
import timber.log.Timber
import kotlin.random.Random

@Composable
internal fun HomeScreen(
    onNotificationsClick: () -> Unit,
    onInviteGroupClick: () -> Unit,
    onJoinGroupClick: () -> Unit,
    onViewMyMissionsMoreClick: () -> Unit,
    onViewMembersMissionsMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeEventHandler(viewModel)

    HomeScreen(
        uiState,
        modifier,
        onUiAction = { uiAction ->
            when (uiAction) { // TODO
                OnNotificationsClick -> onNotificationsClick()
                OnInviteGroupClick -> onInviteGroupClick()
                OnJoinGroupClick -> onJoinGroupClick()
                OnViewMoreMyMissionsClick -> onViewMyMissionsMoreClick()
                OnRequestNewMissionClick -> {}
                is OnViewMoreMemberMissionsClick -> onViewMembersMissionsMoreClick()
                is OnAssignNewMissionClick -> {}
                is OnCompleteMissionClick -> viewModel.completeMission(uiAction.missionId)
            }
        },
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    when (uiState) {
        HomeUiState.Loading -> {
            Box(modifier.fillMaxSize()) {
                ActivityIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is HomeUiState.Success -> with(uiState) {
            HomeScreen(
                user,
                group,
                members,
                rankings,
                myMissions,
                memberMissions,
                modifier,
                onUiAction,
            )
        }
        is HomeUiState.Failure -> with(uiState) { // TODO
            Timber.d(throwable)
        }
    }
}

@Composable
private fun HomeScreen(
    user: User,
    group: Group,
    members: List<Member>,
    rankings: List<LeaderboardEntryUiModel>,
    myMissions: List<MyMissionUiModel>,
    memberMissions: List<MemberMissionUiModel>,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Surface(modifier, color = White) {
        Column {
            StampTopAppBar(
                title = {
                    Image(
                        imageVector = Drawables.Logo,
                        contentDescription = null,
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onUiAction(OnNotificationsClick)
                        }
                    ) {
                        Icon(
                            imageVector = Drawables.Bell,
                            contentDescription = null,
                            tint = Red400,
                        )
                    }
                },
            )

            if (members.size == 1) {
                SoloHome(onUiAction = onUiAction)
            } else {
                val members = members.filter { it.id != user.id }

                GroupHome(
                    userDisplayName = user.displayName.value,
                    groupName = group.name,
                    members,
                    rankings,
                    myMissions,
                    memberMissions,
                    onUiAction = onUiAction,
                )
            }
        }
    }
}

@Composable
private fun SoloHome(
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Box(modifier.fillMaxSize()) {
        var showGroupOnboardingModal by remember {
            mutableStateOf(false)
        }

        EmptyGroupView(
            onClick = {
                showGroupOnboardingModal = true
            },
            modifier = Modifier.padding(16.dp),
        )

        if (showGroupOnboardingModal) {
            GroupOnboardingModalBottomSheet(
                onDismissRequest = {
                    showGroupOnboardingModal = false
                },
                onInviteGroupRequest = {
                    onUiAction(OnInviteGroupClick)
                },
                onJoinGroupRequest = {
                    onUiAction(OnJoinGroupClick)
                },
            )
        }
    }
}

@Composable
private fun GroupHome(
    userDisplayName: String,
    groupName: String,
    members: List<Member>,
    rankings: List<LeaderboardEntryUiModel>,
    myMissions: List<MyMissionUiModel>,
    memberMissions: List<MemberMissionUiModel>,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Column(modifier) {
        val scrollState = rememberScrollState()

        if (scrollState.canScrollBackward) HorizontalDivider(color = Gray25)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(scrollState)
        ) {
            Leaderboard(rankings)

            MyMissions(
                userDisplayName,
                onViewMoreClick = {
                    onUiAction(OnViewMoreMyMissionsClick)
                },
                myMissions,
                onRequestNewMissionClick = {
                    onUiAction(OnRequestNewMissionClick)
                },
                onMissionCompleteClick = { missionId ->
                    onUiAction(OnCompleteMissionClick(missionId))
                }
            )

            MembersMissions(
                userDisplayName,
                groupName,
                memberNamesById = members.associate { member -> member.id to member.displayName.value },
                memberMissions,
                onAssignNewMissionClick = { assigneeId ->
                    onUiAction(OnAssignNewMissionClick(assigneeId))
                },
            )

            Spacer(Modifier.height(24.dp))
        }
    }


}

@Preview
@Composable
private fun GroupHomePreview() {
    StampTheme {
        with(PreviewSamples) {
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

            val myMissions = remember {
                myMissions.filterNot(Mission::isDone)
                    .map { mission ->
                        with(mission) {
                            val assigner = members.first { it.id == assigner }

                            MyMissionUiModel(
                                id,
                                category,
                                title,
                                dueDate,
                                assignerName = assigner.displayName.value,
                            )
                        }
                    }
            }

            HomeScreen(
                user = me,
                group = group,
                members = members,
                rankings = rankings,
                myMissions = myMissions,
                memberMissions = memberMissions,
                modifier = Modifier
                    .fillMaxSize()
                    .background(White),
                onUiAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun SoloHomePreview() {
    StampTheme {
        with(PreviewSamples) {
            HomeScreen(
                user = me,
                group = group,
                members = members.take(1),
                rankings = emptyList(),
                myMissions = emptyList(),
                memberMissions = emptyList(),
                onUiAction = {},
            )
        }
    }
}