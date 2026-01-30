package it.stamp.join.group.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import it.stamp.designsystem.component.showStampSnackbar
import it.stamp.join.group.core.JoinGroupUiEvent
import it.stamp.join.group.core.JoinGroupViewModel
import it.stamp.ui.LocalSnackbarHostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JoinGroupEventHandler(
    viewModel: JoinGroupViewModel,
    onJoinGroupSuccess: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = LocalSnackbarHostState.current

    val displaySnackbar: (String) -> Unit by rememberUpdatedState { message ->
        coroutineScope.launch {
            snackbarHostState.showStampSnackbar(message)
        }
    }

    var pendingDataLossConsent by remember {
        mutableStateOf<JoinGroupUiEvent.DataLossConsentRequired?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                JoinGroupUiEvent.AlreadyInGroup -> snackbarHostState.showStampSnackbar("이미 그룹에 속해있습니다")

                JoinGroupUiEvent.InvalidCode -> snackbarHostState.showStampSnackbar("유효하지 않은 초대 코드입니다")

                is JoinGroupUiEvent.JoinGroupSucceeded -> {
                    snackbarHostState.showStampSnackbar("새로운 그룹에 참여했어요!")

                    delay(250L)

                    onJoinGroupSuccess() // TODO : 홈 화면으로 이동
                }

                is JoinGroupUiEvent.JoinGroupFailed ->
                    displaySnackbar(
                        event.throwable.message
                            ?: "알 수 없는 오류가 발생했습니다"
                    )

                is JoinGroupUiEvent.DataLossConsentRequired -> pendingDataLossConsent = event
            }
        }
    }

    pendingDataLossConsent?.run {
        DataLossConsentDialog(
            onDismissRequest = {
                pendingDataLossConsent = null
            },
            onAccept = {
                viewModel.acceptDataLoss(leavingGroup, joiningGroup)
            },
            onDecline = {
                pendingDataLossConsent = null
            },
        )
    }
}