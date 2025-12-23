package it.stamp.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.component.ActivityIndicator
import it.stamp.designsystem.component.StampSnackbarVisuals
import it.stamp.designsystem.icon.Bell
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.Logo
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.ui.GroupOnboardingView
import it.stamp.home.ui.Leaderboard
import it.stamp.home.ui.MembersMissions
import it.stamp.home.ui.MyMission
import it.stamp.home.ui.MyMissions
import it.stamp.model.membership.Group
import it.stamp.model.membership.Member
import it.stamp.model.mission.Mission
import it.stamp.model.stamp.LeaderboardMember
import it.stamp.ui.LocalMembership
import it.stamp.ui.LocalSnackbarHostState
import it.stamp.ui.LocalUser
import it.stamp.ui.TopAppBar

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val user = LocalUser.current
    val membership = LocalMembership.current

    LaunchedEffect(user, membership) {
        if (user != null && membership != null) {
            viewModel.setUserAndMembership(user, membership)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = LocalSnackbarHostState.current

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is HomeUiEvent.MissionCompleted -> with(uiEvent) {
                    val missionTitle = mission.title

                    val result = snackbarHostState.showSnackbar(
                        StampSnackbarVisuals.MissionCompletion(
                            missionTitle,
                            message = context.getString(R.string.mission_completed),
                            actionLabel = context.getString(R.string.cancel),
                        )
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        // cancel
                    }
                }
                is HomeUiEvent.MissionCanceled -> {}
                is HomeUiEvent.OperationFailed -> {

                }
            }
        }
    }

    HomeScreen(
        uiState,
        modifier,
        onUiAction = { uiAction ->
            when (uiAction) { // TODO
                HomeUiAction.OnNotificationsClick -> {}
                HomeUiAction.OnGroupOnboardingClick -> {}
                HomeUiAction.OnViewMyMissionsMoreClick -> {}
                HomeUiAction.OnRequestNewMissionClick -> {}
                is HomeUiAction.OnCompleteMissionClick -> with(uiAction) {
                    viewModel.completeMission(missionId)
                }
                is HomeUiAction.OnViewMemberMissionsMoreClick -> {}
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
                membersMissions,
                modifier,
                onUiAction,
            )
        }
        is HomeUiState.Failure -> {
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
        TopAppBar(
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

        val scrollState = rememberScrollState()

        if (scrollState.canScrollBackward) HorizontalDivider(color = Gray25)

        Column(
            modifier = Modifier
                .weight(1F)
                .verticalScroll(scrollState)
        ) {
            if (members.none { !it.isLeader }) {
                GroupOnboardingView(
                    onClick = {
                        onUiAction(HomeUiAction.OnGroupOnboardingClick)
                    },
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Leaderboard(user, rankings)

                val myMissions = remember(myMissions, members) {
                    myMissions.filter { !it.isCompleted }.map { mission ->
                        with(mission) {
                            val assignerName = members.first { it.id == assigner }.displayName

                            MyMission(
                                id = id,
                                category = category,
                                title = title,
                                dueDate = dueDate,
                                assignerName = assignerName,
                            )
                        }
                    }
                }

                MyMissions(
                    userDisplayName = user.displayName,
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
                    userDisplayName = user.displayName,
                    groupName = group.name,
                    members = members - user,
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
                user = sampleMe,
                group = sampleGroup,
                members = sampleMembers,
                rankings = sampleRankings,
                myMissions = sampleMyMissions,
                membersMissions = sampleMemberMissions,
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
            user = sampleMe,
            group = sampleGroup,
            members = emptyList(),
            rankings = sampleRankings,
            myMissions = emptyList(),
            memberMissions = sampleMemberMissions,
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            onUiAction = {},
        )
    }
}