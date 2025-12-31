package it.stamp.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.ButtonSize
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.component.StampModalBottomSheet
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.Red50
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.home.R

enum class GroupOnboarding {
    Invite,
    Join,
}

@Composable
fun GroupOnboardingModalBottomSheet(
    onDismissRequest: () -> Unit,
    onInviteGroupClick: () -> Unit,
    onJoinGroupClick: () -> Unit,
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
                    GroupOnboarding.Invite -> onInviteGroupClick()
                    GroupOnboarding.Join -> onJoinGroupClick()
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
            DestinationButton(
                selected = groupOnboarding == GroupOnboarding.Invite,
                label = stringResource(R.string.invite),
                description = stringResource(R.string.invite_group_description),
                onClick = {
                    onOnboardingActionClick(GroupOnboarding.Invite)
                },
            )

            DestinationButton(
                selected = groupOnboarding == GroupOnboarding.Join,
                label = stringResource(R.string.join),
                description = stringResource(R.string.join_group_description),
                onClick = {
                    onOnboardingActionClick(GroupOnboarding.Join)
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
            size = ButtonSize.Large,
        ) {
            Text(stringResource(R.string.confirm))
        }
    }
}

@Composable
private fun DestinationButton(
    selected: Boolean,
    label: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = if (selected) {
        Red400
    } else {
        Gray50
    },
    backgroundColor: Color = if (selected) {
        Red50
    } else {
        White
    },
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.CenterVertically),
    ) {
        Text(
            text = label,
            color = Gray800,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = description,
            color = Gray500,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun GroupOnboardingModalBottomSheetPreview() {
    StampTheme {
        GroupOnboardingModalBottomSheet(
            onDismissRequest = {},
            onInviteGroupClick = {},
            onJoinGroupClick = {},
        )
    }
}