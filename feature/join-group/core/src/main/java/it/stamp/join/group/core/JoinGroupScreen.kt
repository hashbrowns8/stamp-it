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
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.trimmedLength
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import it.stamp.designsystem.component.PrimaryButton
import it.stamp.designsystem.component.StampTextField
import it.stamp.designsystem.icon.ArrowLeft
import it.stamp.designsystem.icon.CharacterRed
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.model.membership.InviteCode
import it.stamp.ui.StampTopAppBar

@Composable
internal fun JoinGroupScreen(
    modifier: Modifier = Modifier,
    viewModel: JoinGroupViewModel = hiltViewModel(),
) {
    viewModel
}

@Composable
private fun JoinGroupScreen(
    onJoinClick: (InviteCode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(White)
            .imePadding(),
    ) {
        StampTopAppBar(
            title = {
                Text("초대 받기")
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Drawables.ArrowLeft,
                        contentDescription = null,
                    )
                }
            }
        )

        val textFieldState = rememberTextFieldState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Bottom),
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

                    Row(Modifier.align(Alignment.TopCenter)) {
                        repeat(4) {
                            Image(
                                painter = Drawables.CharacterRed,
                                contentDescription = null,
                                modifier = Modifier.width(64.dp),
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                    }
                }

                Text(
                    text = "초대 받을 그룹의 코드를 입력해주세요",
                    color = Gray800,
                    style = MaterialTheme.typography.bodyMedium,
                )

                StampTextField(
                    textFieldState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    label = {
                        Text("초대코드")
                    },
                    inputTransformation = InputTransformation.maxLength(8),
                    lineLimits = TextFieldLineLimits.SingleLine,
                )
            }

            Box(Modifier.weight(0.75F))
        }

        PrimaryButton(
            onClick = {
                val inviteCode = InviteCode(textFieldState.text.toString())
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            enabled = textFieldState.text.trimmedLength() == 8,
        ) {
            Text("입장하기")
        }
    }
}

@Preview
@Composable
private fun JoinGroupScreenPreview() {
    StampTheme {
        JoinGroupScreen(
            onJoinClick = {
            },
        )
    }
}