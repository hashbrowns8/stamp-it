package it.stamp.membership.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import it.stamp.designsystem.component.StampAlertDialog
import it.stamp.model.ids.UserId
import it.stamp.model.membership.Member

@Composable
fun MembershipMenuDialog(
    member: Member?,
    onDismiss: () -> Unit,
    onTransferLeadership: (UserId) -> Unit,
    onRemoveMember: (UserId) -> Unit,
) {
    if (member == null) return

    var pendingRequest by remember {
        mutableStateOf<MembershipMenu?>(null)
    }

    MembershipMenuModalBottomSheet(
        onDismissRequest = onDismiss,
        onTransferLeadershipRequest = {
            pendingRequest = MembershipMenu.TRANSFER_LEADERSHIP
        },
        onRemoveMemberRequest = {
            pendingRequest = MembershipMenu.REMOVE_MEMBER
        },
    )

    pendingRequest?.let { request ->
        val (text, confirm) = when (request) {
            MembershipMenu.TRANSFER_LEADERSHIP -> {
                "${member.displayName.value}님을\n그룹 리더로 위임할까요?" to "위임하기"
            }
            MembershipMenu.REMOVE_MEMBER -> "${member.displayName.value}님을\n그룹에서 내보낼까요?" to "내보내기"
        }

        StampAlertDialog(
            onDismissRequest = {
                pendingRequest = null
            },
            text = text,
            confirm = confirm,
            onConfirmClick = {
                when (request) {
                    MembershipMenu.TRANSFER_LEADERSHIP -> onTransferLeadership(member.id)
                    MembershipMenu.REMOVE_MEMBER -> onRemoveMember(member.id)
                }
            },
            dismiss = "취소",
            onDismissClick = {
                pendingRequest = null
            },
        )
    }
}