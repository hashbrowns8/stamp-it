package it.stamp.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.CheckCircle
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.designsystem.theme.bodyExtraSmall

sealed interface StampSnackbarVisuals : SnackbarVisuals {
    data class Default(
        override val message: String,
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = false,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : StampSnackbarVisuals

    data class MissionCompletion( // TODO : Mission 관련 스낵바 (?)
        val missionTitle: String,
        override val message: String,
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = false,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : StampSnackbarVisuals
}

@Composable
fun StampSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) = with(snackbarData) {
    val actionLabel = visuals.actionLabel

    SnackbarLayout(
        message = {
            ProvideTextStyle(
                MaterialTheme.typography.labelSmall.merge(color = Gray800)
            ) {
                when (val visuals = visuals) {
                    is StampSnackbarVisuals.Default -> {
                        Text(
                            text = visuals.message,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                    is StampSnackbarVisuals.MissionCompletion -> with(visuals) {
                        MissionCompletionMessage(missionTitle, message)
                    }
                }
            }
        },
        action = if (actionLabel != null) {
            {
                Text(
                    text = actionLabel,
                    modifier = Modifier
                        .clickable(onClick = ::performAction)
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
                    color = Red400,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyExtraSmall,
                )
            }
        } else {
            null
        },
        modifier,
    )
}

@Composable
private fun SnackbarLayout(
    message: @Composable () -> Unit,
    action: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(44.dp)
                .background(White, CircleShape)
                .border(1.dp, Red400, CircleShape)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    imageVector = Drawables.CheckCircle,
                    contentDescription = null,
                )

                message()
            }

            action?.invoke()
        }
    }
}

@Composable
private fun MissionCompletionMessage(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeLayout(modifier) { constraints ->
        val prefix = subcompose("prefix") {
            Text("\'")
        }[0].measure(constraints)

        val suffix = subcompose("suffix") {
            Text("\' $message",)
        }[0].measure(constraints)

        val maxWidth = (constraints.maxWidth - prefix.width - suffix.width)
            .coerceAtLeast(0)

        val title = subcompose("title") {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }[0].measure(constraints.copy(maxWidth = maxWidth))

        val height = maxOf(prefix.height, title.height, suffix.height)

        layout(constraints.maxWidth, height) {
            var x = 0
            prefix.place(x, 0)
            x += prefix.width

            title.place(x, 0)
            x += title.width

            suffix.place(x, 0)
        }
    }
}

@Preview
@Composable
private fun StampSnackbarPreview() {
    StampTheme {
        val snackbarHostState = remember {
            SnackbarHostState()
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = { data ->
                        StampSnackbar(data)
                    }
                )
            },
            content = {},
        )

        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar(
                StampSnackbarVisuals.Default("미션 조르기가 전달되지 않았어요. 다시 시도해주세요.")
            )
        }
    }
}