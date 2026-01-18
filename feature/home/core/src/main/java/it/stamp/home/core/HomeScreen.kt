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
import it.stamp.home.core.ui.EmptyGroupMemberView
import it.stamp.home.core.ui.GroupOnboardingModalBottomSheet
import it.stamp.home.core.ui.Leaderboard
import it.stamp.home.core.ui.MembersMissions
import it.stamp.home.core.ui.MyMission
import it.stamp.home.core.ui.MyMissions
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.ui.PreviewSamples
import timber.log.Timber

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
                HomeUiAction.OnRetryClick -> {
                    viewModel.retry()
                }
                HomeUiAction.OnNotificationsClick -> onNotificationsClick()
                HomeUiAction.OnInviteGroupClick -> onInviteGroupClick()
                HomeUiAction.OnJoinGroupClick -> onJoinGroupClick()
                HomeUiAction.OnViewMyMissionsMoreClick -> onViewMyMissionsMoreClick()
                HomeUiAction.OnRequestNewMissionClick -> {}
                is HomeUiAction.OnCompleteMissionClick -> with(uiAction) {
                    viewModel.completeMission(missionId)
                }
                is HomeUiAction.OnViewMembersMissionsMoreClick -> onViewMembersMissionsMoreClick()
                is HomeUiAction.OnAssignNewMissionClick -> {}
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
            Content(
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
private fun Content(
    user: Member,
    group: Group,
    members: List<Member>,
    rankings: List<LeaderboardMember>,
    myMissions: List<Mission>,
    memberMissions: List<Mission>,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Column(modifier) {
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
                        onUiAction(HomeUiAction.OnNotificationsClick)
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

        if (members.none { !it.isLeader }) {
            Box {
                var isOnboardingSheetOpen by remember {
                    mutableStateOf(false)
                }

                EmptyGroupMemberView(
                    onClick = {
                        isOnboardingSheetOpen = true
                    },
                    modifier = Modifier.padding(16.dp),
                )

                if (isOnboardingSheetOpen) {
                    GroupOnboardingModalBottomSheet(
                        onDismissRequest = {
                            isOnboardingSheetOpen = false
                        },
                        onInviteGroupClick = {
                            onUiAction(HomeUiAction.OnInviteGroupClick)
                        },
                        onJoinGroupClick = {
                            onUiAction(HomeUiAction.OnJoinGroupClick)
                        },
                    )
                }
            }
        } else {
            val scrollState = rememberScrollState()

            if (scrollState.canScrollBackward) HorizontalDivider(color = Gray25)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .verticalScroll(scrollState)
            ) {
                Leaderboard(user, rankings)

                val myMissions = remember(myMissions, members) {
                    myMissions.filter { !it.isCompleted }.map { mission ->
                        with(mission) {
                            val assigner = members.first { it.id == assigner }

                            MyMission(
                                id,
                                category,
                                title,
                                dueDate,
                                assignerName = assigner.displayName.value,
                            )
                        }
                    }
                }

                MyMissions(
                    userDisplayName = user.displayName.value,
                    onViewMoreClick = {
                        onUiAction(HomeUiAction.OnViewMyMissionsMoreClick)
                    },
                    missions = myMissions,
                    onRequestNewMissionClick = {
                        onUiAction(HomeUiAction.OnRequestNewMissionClick)
                    },
                    onMissionCompleteClick = { missionId ->
                        onUiAction(HomeUiAction.OnCompleteMissionClick(missionId))
                    }
                )

                MembersMissions(
                    userDisplayName = user.displayName.value,
                    groupName = group.name,
                    members,
                    memberMissions,
                    onAssignNewMissionClick = {
                        onUiAction(HomeUiAction.OnAssignNewMissionClick(it))
                    },
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    StampTheme {
        HomeScreen(
            HomeUiState.Success(
                user = PreviewSamples.MeAsMember,
                group = PreviewSamples.Group,
                members = PreviewSamples.Members,
                rankings = PreviewSamples.Rankings,
                myMissions = PreviewSamples.MyMissions,
                memberMissions = PreviewSamples.MemberMissions,
            ),
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            onUiAction = {},
        )
    }
}

@Preview
@Composable
private fun HomeScreenOnlyMePreview() {
    StampTheme {
        Content(
            user = PreviewSamples.MeAsMember,
            group = PreviewSamples.Group,
            members = emptyList(),
            rankings = PreviewSamples.Rankings,
            myMissions = emptyList(),
            memberMissions = PreviewSamples.MemberMissions,
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            onUiAction = {},
        )
    }
}