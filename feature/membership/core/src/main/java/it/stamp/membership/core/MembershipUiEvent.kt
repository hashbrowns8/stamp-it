package it.stamp.membership.core

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import it.stamp.designsystem.component.showStampSnackbar
import it.stamp.model.ids.UserId
import it.stamp.ui.LocalSnackbarHostState

sealed interface MembershipUiEvent {
    data object LeadershipTransferred : MembershipUiEvent
    data class MemberRemoved(val memberId: UserId) : MembershipUiEvent
    data class MemberRemovalFailed(val throwable: Throwable) : MembershipUiEvent
}

@Composable
fun MembershipEventHandler(
    viewModel: MembershipViewModel,
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                MembershipUiEvent.LeadershipTransferred -> {
                    snackbarHostState.showStampSnackbar("리더 위임이 완료되었습니다")
                }

                is MembershipUiEvent.MemberRemovalFailed -> {
                    snackbarHostState.showStampSnackbar("문제가 발생했어요. 다시 시도해 주세요.")
                }

                is MembershipUiEvent.MemberRemoved -> {
                    snackbarHostState.showStampSnackbar("멤버를 내보냈습니다")
                }
            }
        }
    }
}