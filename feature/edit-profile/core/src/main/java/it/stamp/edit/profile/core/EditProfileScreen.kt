package it.stamp.edit.profile.core

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.component.StampTopAppBar
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.White
import it.stamp.edit.profile.core.ui.EditAvatar
import it.stamp.edit.profile.core.ui.EditText
import it.stamp.model.user.Avatar
import it.stamp.ui.BackButton

@Composable
internal fun EditProfileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EditProfileScreen(
        uiState = uiState,
        modifier = modifier,
        onUiAction = { action ->
            when (action) {
                OnBackClick -> onBack()
                is OnAvatarChange -> viewModel.updateAvatar(action.avatar)
                is OnDisplayNameChange -> viewModel.updateDisplayName(action.displayName)
                is OnGroupNameChange -> viewModel.updateGroupName(action.groupName)
                OnCompleteClick -> viewModel.complete()
            }
        },
    )
}

@Composable
private fun EditProfileScreen(
    uiState: EditProfileUiState,
    modifier: Modifier = Modifier,
    onUiAction: OnUiAction,
) {
    Scaffold(
        modifier,
        topBar = {
            StampTopAppBar(
                title = {
                    Text(stringResource(R.string.edit_profile_title))
                },
                navigationIcon = {
                    BackButton(
                        onClick = {
                            onUiAction(OnBackClick)
                        },
                    )
                },
            )
        },
        containerColor = White,
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .clickable {
                    focusManager.clearFocus()
                }
                .imePadding(),
        ) {
            when (uiState) {
                EditProfileUiState.Loading -> {}
                is EditProfileUiState.Success -> with(uiState) {
                    EditProfileScreen(
                        avatar,
                        displayName,
                        groupName,
                        canRenameGroup,
                        canComplete,
                        onUiAction = { action ->
                            if (action is OnAvatarChange || action == OnCompleteClick) {
                                focusManager.clearFocus()
                            }

                            onUiAction(action)
                        },
                    )
                }
                is EditProfileUiState.Failure -> {}
            }
        }
    }
}

@Composable
private fun EditProfileScreen(
    avatar: Avatar,
    displayName: String,
    groupName: String,
    canRenameGroup: Boolean,
    canComplete: Boolean,
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
                .padding(bottom = 48.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            EditAvatar(
                avatar,
                onAvatarChange = { avatar ->
                    onUiAction(OnAvatarChange(avatar))
                },
            )

            Spacer(Modifier.height(24.dp))

            EditText(
                label = stringResource(R.string.display_name),
                value = displayName,
                onValueChange = { displayName ->
                    onUiAction(OnDisplayNameChange(displayName))
                },
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(32.dp))

            EditText(
                label = stringResource(R.string.group_name),
                value = groupName,
                onValueChange = { groupName ->
                    onUiAction(OnGroupNameChange(groupName))
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = canRenameGroup,
            )
        }

        PrimaryButton(
            onClick = {
                onUiAction(OnCompleteClick)
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                .fillMaxWidth(),
            enabled = canComplete,
        ) {
            Text(stringResource(R.string.edit))
        }
    }
}