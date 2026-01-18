package it.stamp.join.group.core

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.component.StampTextField
import it.stamp.designsystem.component.StampTopAppBar
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.join.group.core.ui.JoinGroupEventHandler
import it.stamp.join.group.core.ui.JoinGroupInProgress
import it.stamp.model.membership.Group
import it.stamp.model.membership.InviteCode

@Composable
internal fun JoinGroupScreen(
    onBackClick: () -> Unit,
    onJoinGroupSuccess: (group: Group) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinGroupViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    JoinGroupEventHandler(
        viewModel,
        onJoinGroupSuccess,
    )

    JoinGroupScreen(
        uiState,
        onBackClick,
        onInviteCodeChange = viewModel::updateInviteCode,
        onJoinGroupClick = viewModel::joinGroup,
        modifier,
    )
}

@Composable
private fun JoinGroupScreen(
    uiState: JoinGroupUiState,
    onBackClick: () -> Unit,
    onInviteCodeChange: (String) -> Unit,
    onJoinGroupClick: (InviteCode) -> Unit,
    modifier: Modifier = Modifier,
) = with(uiState) {
    when {
        inProgress -> JoinGroupInProgress()
        else -> {
            JoinGroupScreen(
                inviteCode,
                onBackClick,
                onInviteCodeChange,
                onJoinGroupClick,
                modifier,
            )
        }
    }
}

@Composable
private fun JoinGroupScreen(
    inviteCode: String,
    onBackClick: () -> Unit,
    onInviteCodeChange: (String) -> Unit,
    onJoinGroupClick: (InviteCode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(White)
            .imePadding(),
    ) {
        StampTopAppBar(
            title = {
                Text(stringResource(R.string.join_group_title))
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Drawables.ArrowLeft,
                        contentDescription = null,
                    )
                }
            }
        )

        val textFieldState = rememberTextFieldState()

        LaunchedEffect(inviteCode) {
            if (textFieldState.text == inviteCode) return@LaunchedEffect

            textFieldState.setTextAndPlaceCursorAtEnd(inviteCode)
        }

        LaunchedEffect(textFieldState) {
            snapshotFlow { textFieldState.text.toString() }
                .collect(onInviteCodeChange)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .verticalScroll(rememberScrollState())
                .padding(top = 72.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box {
                Canvas(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 56.dp)
                        .width(280.dp)
                        .height(50.dp)
                ) {
                    drawOval(color = Black.copy(alpha = 0.1F))
                }

                Row(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    listOf(
                        Drawables.CharacterRed,
                        Drawables.CharacterBlue,
                        Drawables.CharacterYellow,
                        Drawables.CharacterPurple,
                    ).forEach { character ->
                        Image(
                            character,
                            contentDescription = null,
                            modifier = Modifier.width(64.dp),
                            contentScale = ContentScale.FillWidth,
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.join_group_with_invite_code_message),
                color = Gray800,
                style = MaterialTheme.typography.bodyMedium,
            )

            StampTextField(
                textFieldState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.invite_code))
                },
                inputTransformation = InputTransformation.maxLength(8),
                lineLimits = TextFieldLineLimits.SingleLine,
            )
        }

        val inviteCode by remember {
            derivedStateOf {
                runCatching {
                    InviteCode(textFieldState.text.toString())
                }.getOrElse {
                    // TODO : 오류 메시지
                    null
                }
            }
        }

        PrimaryButton(
            onClick = {
                inviteCode?.let(onJoinGroupClick)
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                .fillMaxWidth(),
            enabled = inviteCode != null,
        ) {
            Text(stringResource(R.string.enter))
        }
    }
}

@Preview
@Composable
private fun JoinGroupScreenPreview() {
    StampTheme {
        JoinGroupScreen(
            inviteCode = String(),
            onBackClick = {
            },
            onInviteCodeChange = {
            },
            onJoinGroupClick = {
            },
        )
    }
}