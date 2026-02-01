package it.stamp.home.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.component.StampModalBottomSheet
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.home.core.R
import it.stamp.ui.SelectableButton

enum class GroupOnboarding {
    INVITE_MEMBER,
    JOIN_GROUP;
}

@Composable
fun GroupOnboardingModalBottomSheet(
    onDismissRequest: () -> Unit,
    onInviteGroupRequest: () -> Unit,
    onJoinGroupRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    StampModalBottomSheet(
        onDismissRequest,
        modifier,
        sheetState,
    ) {
        var groupOnboarding by remember {
            mutableStateOf<GroupOnboarding?>(null)
        }

        SheetContent(
            groupOnboarding,
            onOnboardingActionClick = {
                groupOnboarding = it
            },
            onConfirmClick = { groupOnboarding ->
                when (groupOnboarding) {
                    GroupOnboarding.INVITE_MEMBER -> onInviteGroupRequest()
                    GroupOnboarding.JOIN_GROUP -> onJoinGroupRequest()
                }

                onDismissRequest()
            }
        )
    }
}

@Composable
private fun SheetContent(
    groupOnboarding: GroupOnboarding?,
    onOnboardingActionClick: (GroupOnboarding) -> Unit,
    onConfirmClick: (GroupOnboarding) -> Unit,
) {
    Column(
        Modifier.padding(
            start = 16.dp,
            top = 8.dp,
            end = 16.dp,
            bottom = 32.dp,
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.group_onboarding),
                color = Gray800,
                style = MaterialTheme.typography.titleSmall,
            )

            Text(
                text = stringResource(R.string.group_onboarding_description),
                color = Gray500,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SelectableButton(
                selected = groupOnboarding == GroupOnboarding.INVITE_MEMBER,
                label = stringResource(R.string.invite),
                description = stringResource(R.string.invite_group_description),
                onClick = {
                    onOnboardingActionClick(GroupOnboarding.INVITE_MEMBER)
                },
            )

            SelectableButton(
                selected = groupOnboarding == GroupOnboarding.JOIN_GROUP,
                label = stringResource(R.string.join),
                description = stringResource(R.string.join_group_description),
                onClick = {
                    onOnboardingActionClick(GroupOnboarding.JOIN_GROUP)
                },
            )
        }

        PrimaryButton(
            onClick = {
                if (groupOnboarding == null) return@PrimaryButton

                onConfirmClick(groupOnboarding)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = groupOnboarding != null,
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Preview
@Composable
private fun GroupOnboardingModalBottomSheetPreview() {
    StampTheme {
        GroupOnboardingModalBottomSheet(
            onDismissRequest = {},
            onInviteGroupRequest = {},
            onJoinGroupRequest = {},
        )
    }
}