package it.stamp.membership.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.component.StampTopAppBar
import it.stamp.designsystem.theme.White
import it.stamp.membership.core.ui.MemberListItem
import it.stamp.membership.core.ui.MembershipMenuDialog
import it.stamp.model.membership.Member
import it.stamp.model.user.User
import it.stamp.ui.BackButton

@Composable
internal fun MembershipScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MembershipViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var pendingAction by remember {
        mutableStateOf<OnMemberMenuClick?>(null)
    }

    MembershipScreen(
        uiState,
        modifier,
        onUiAction = { action ->
            when (action) {
                OnBackClick -> onBack()
                is OnMemberMenuClick -> {
                    pendingAction = action
                }
            }
        }
    )

    MembershipMenuDialog(
        member = pendingAction?.member,
        onDismiss = {
            pendingAction = null
        },
        onTransferLeadership = { memberId ->
            viewModel.transferLeadership(memberId)

            pendingAction = null
        },
        onRemoveMember = { memberId ->
            viewModel.removeMember(memberId)

            pendingAction = null
        },
    )

    MembershipEventHandler(viewModel)
}

@Composable
private fun MembershipScreen(
    uiState: MembershipUiState,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Scaffold(
        modifier,
        topBar = {
            StampTopAppBar(
                title = {
                    Text(stringResource(R.string.membership_screen_title))
                },
                navigationIcon = {
                    BackButton(
                        onClick = {
                            onUiAction(OnBackClick)
                        }
                    )
                },
            )
        },
        containerColor = White,
    ) { innerPadding ->
        when (uiState) {
            MembershipUiState.Loading -> {
            }
            is MembershipUiState.Success -> with(uiState) {
                MembershipScreen(
                    user,
                    members,
                    canManageMember,
                    modifier = Modifier.padding(innerPadding),
                    onUiAction,
                )
            }
            is MembershipUiState.Failure -> {
            }
        }
    }
}

@Composable
private fun MembershipScreen(
    user: User,
    members: List<Member>,
    canManageMember: Boolean,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    LazyColumn(
        modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(members) { member ->
            MemberListItem(
                member,
                canManageMember = canManageMember && user.id != member.id,
                onMenuClick = {
                    onUiAction(OnMemberMenuClick(member))
                },
            )
        }
    }
}