package it.stamp.membership.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import it.stamp.designsystem.theme.StampTheme
import it.stamp.membership.core.R
import it.stamp.ui.SelectableButton

enum class MembershipMenu {
    TRANSFER_LEADERSHIP,
    REMOVE_MEMBER;
}

@Composable
fun MembershipMenuModalBottomSheet(
    onDismissRequest: () -> Unit,
    onTransferLeadershipRequest: () -> Unit,
    onRemoveMemberRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StampModalBottomSheet(
        onDismissRequest,
        modifier,
    ) {
        Column(
            Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            var selectedMenu by remember {
                mutableStateOf<MembershipMenu?>(null)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SelectableButton(
                    selected = selectedMenu == MembershipMenu.TRANSFER_LEADERSHIP,
                    label = stringResource(R.string.transfer_leadership),
                    onClick = {
                        selectedMenu = MembershipMenu.TRANSFER_LEADERSHIP
                    },
                )

                SelectableButton(
                    selected = selectedMenu == MembershipMenu.REMOVE_MEMBER,
                    label = stringResource(R.string.remove_member),
                    onClick = {
                        selectedMenu = MembershipMenu.REMOVE_MEMBER
                    },
                )
            }

            PrimaryButton(
                onClick = {
                    when (selectedMenu) {
                        MembershipMenu.TRANSFER_LEADERSHIP -> onTransferLeadershipRequest()
                        MembershipMenu.REMOVE_MEMBER -> onRemoveMemberRequest()
                        null -> {}
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedMenu != null,
            ) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}

@Preview
@Composable
private fun MembershipMenuModalBottomSheetPreview() {
    StampTheme {
        MembershipMenuModalBottomSheet(
            onDismissRequest = {},
            onTransferLeadershipRequest = {},
            onRemoveMemberRequest = {},
        )
    }
}