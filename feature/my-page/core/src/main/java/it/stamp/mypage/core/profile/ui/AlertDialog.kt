package it.stamp.mypage.core.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.stamp.designsystem.component.StampAlertDialog
import it.stamp.mypage.core.R

@Composable
internal fun LeaveGroupAlertDialog(
    onDismissRequest: () -> Unit,
    groupName: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = onDismissRequest,
) {
    AlertDialog(
        onDismissRequest,
        title = stringResource(R.string.leave_group_alert_title, groupName),
        text = stringResource(R.string.leave_group_alert_text),
        onDismiss,
        onConfirm,
        modifier,
    )
}

@Composable
internal fun SignOutAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = onDismissRequest,
) {
    AlertDialog(
        onDismissRequest,
        title = stringResource(R.string.sign_out_alert_title),
        text = stringResource(R.string.sign_out_alert_text),
        onDismiss,
        onConfirm,
        modifier,
    )
}

@Composable
internal fun DeleteAccountAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = onDismissRequest,
) {
    AlertDialog(
        onDismissRequest,
        title = stringResource(R.string.delete_account_alert_title),
        text = stringResource(R.string.delete_account_alert_text),
        onDismiss,
        onConfirm,
        modifier,
    )
}

@Composable
private fun AlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StampAlertDialog(
        onDismissRequest,
        text,
        confirm = stringResource(R.string.confirm),
        onConfirmClick = onConfirm,
        modifier,
        title,
        dismiss = stringResource(R.string.cancel),
        onDismissClick = onDismiss,
    )
}