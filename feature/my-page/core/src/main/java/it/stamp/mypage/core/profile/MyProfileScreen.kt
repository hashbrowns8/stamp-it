package it.stamp.mypage.core.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.model.membership.Group
import it.stamp.model.user.User
import it.stamp.mypage.core.MyProfileUiAction
import it.stamp.mypage.core.R
import it.stamp.mypage.core.profile.ui.DeleteAccountAlertDialog
import it.stamp.mypage.core.profile.ui.LeaveGroupAlertDialog
import it.stamp.mypage.core.profile.ui.MenuItem
import it.stamp.mypage.core.profile.ui.MenuSection
import it.stamp.mypage.core.profile.ui.SignOutAlertDialog
import it.stamp.mypage.core.profile.ui.UserProfile
import it.stamp.ui.PreviewSamples

@Composable
internal fun MyProfileScreen(
    navigateToEditProfile: () -> Unit,
    navigateToManageMembers: () -> Unit,
    navigateToInviteMember: () -> Unit,
    navigateToJoinGroup: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var pendingAction by remember {
        mutableStateOf<MyProfileUiAction?>(null)
    }

    MyProfileScreen(
        uiState,
        modifier,
        onUiAction = { action ->
            when (action) {
                MyProfileUiAction.OnEditProfileClick -> navigateToEditProfile()
                MyProfileUiAction.OnManageMembersClick -> navigateToManageMembers()
                MyProfileUiAction.OnInviteMemberClick -> navigateToInviteMember()
                MyProfileUiAction.OnJoinGroupClick -> navigateToJoinGroup()
                is MyProfileUiAction.OnLeaveGroupClick -> pendingAction = action
                MyProfileUiAction.OnSignOutClick -> pendingAction = action
                MyProfileUiAction.OnDeleteAccountClick -> pendingAction = action
            }
        },
    )

    when (val action = pendingAction) {
        is MyProfileUiAction.OnLeaveGroupClick -> {
            LeaveGroupAlertDialog(
                onDismissRequest = {
                    pendingAction = null
                },
                groupName = action.groupName,
                onConfirm = {
                    pendingAction = null

                    viewModel.leaveGroup()
                }
            )
        }
        MyProfileUiAction.OnSignOutClick -> {
            SignOutAlertDialog(
                onDismissRequest = {
                    pendingAction = null
                },
                onConfirm = {
                    pendingAction = null

                    viewModel.signOut()
                },
            )
        }
        MyProfileUiAction.OnDeleteAccountClick -> {
            DeleteAccountAlertDialog(
                onDismissRequest = {
                    pendingAction = null
                },
                onConfirm = {
                    pendingAction = null

                    viewModel.deleteAccount()
                },
            )
        }
        else -> {}
    }
}

@Composable
private fun MyProfileScreen(
    uiState: MyProfileUiState,
    modifier: Modifier = Modifier,
    onUiAction: (MyProfileUiAction) -> Unit,
) {
    when (uiState) {
        MyProfileUiState.Loading -> {}

        is MyProfileUiState.Success -> with(uiState) {
            MyProfileScreen(
                user,
                group,
                modifier,
                onUiAction,
            )
        }

        is MyProfileUiState.Failure -> {}
    }
}

@Composable
private fun MyProfileScreen(
    user: User,
    group: Group,
    modifier: Modifier = Modifier,
    onUiAction: (MyProfileUiAction) -> Unit,
) {
    Surface(modifier, color = White) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 28.dp),
                contentAlignment = Alignment.Center,
            ) {
                UserProfile(
                    avatar = user.avatar,
                    groupName = group.name,
                    displayName = user.displayName.value,
                    onEditClick = {
                        onUiAction(MyProfileUiAction.OnEditProfileClick)
                    },
                )
            }

            HorizontalDivider(thickness = 8.dp, color = Gray25)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                MenuSection(stringResource(R.string.group_memberships_management)) {
                    arrayOf(
                        Triple(
                            stringResource(R.string.member_management_title),
                            stringResource(R.string.member_management_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnManageMembersClick)
                        },
                        Triple(
                            stringResource(R.string.invite_member_title),
                            stringResource(R.string.invite_member_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnInviteMemberClick)
                        },
                        Triple(
                            stringResource(R.string.join_group_title),
                            stringResource(R.string.join_group_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnJoinGroupClick)
                        },
                    ).forEach { (label, description, onClick) ->
                        MenuItem(
                            label,
                            description,
                            onClick
                        )
                    }
                }

                HorizontalDivider(color = Gray50)

                MenuSection(stringResource(R.string.user_membership_account_management)) {
                    listOf(
                        Triple(
                            stringResource(R.string.leave_group_title),
                            stringResource(R.string.leave_group_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnLeaveGroupClick(group.name))
                        },
                        Triple(
                            stringResource(R.string.sign_out_title),
                            stringResource(R.string.sign_out_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnSignOutClick)
                        },
                        Triple(
                            stringResource(R.string.delete_account_title),
                            stringResource(R.string.delete_account_description),
                        ) {
                            onUiAction(MyProfileUiAction.OnDeleteAccountClick)
                        },
                    ).forEach { (label, description, onClick) ->
                        MenuItem(
                            label,
                            description,
                            onClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyProfileScreenPreview() {
    StampTheme {
        with(PreviewSamples) {
            MyProfileScreen(
                user = me,
                group = group,
                onUiAction = {}
            )
        }
    }
}