package it.stamp.join.group.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import it.stamp.designsystem.component.displaySnackbar
import it.stamp.join.group.core.JoinGroupUiEvent
import it.stamp.join.group.core.JoinGroupViewModel
import it.stamp.ui.LocalSnackbarHostState
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
            snackbarHostState.displaySnackbar(message)
        }
    }

    val dataLossConsent = remember {
        mutableStateOf<JoinGroupUiEvent.RequestDataLossConsent?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                JoinGroupUiEvent.AlreadyInGroup -> displaySnackbar("이미 그룹에 속해있습니다")

                JoinGroupUiEvent.InvalidCode -> displaySnackbar("유효하지 않은 초대 코드입니다")

                is JoinGroupUiEvent.JoinGroupSuccess -> onJoinGroupSuccess()

                is JoinGroupUiEvent.JoinGroupFailure ->
                    displaySnackbar(
                        event.throwable.message
                            ?: "알 수 없는 오류가 발생했습니다"
                    )

                is JoinGroupUiEvent.RequestDataLossConsent -> {
                    dataLossConsent.value = event
                }
            }
        }
    }

    dataLossConsent.value?.let { event ->
        DataLossConsentDialog(
            onDismissRequest = {
                dataLossConsent.value = null
            },
            onAccept = {
                with(event) {
                    viewModel.acceptDataLoss(leavingGroup, joiningGroup)
                }
            },
            onDecline = {
                dataLossConsent.value = null
            },
        )
    }
}