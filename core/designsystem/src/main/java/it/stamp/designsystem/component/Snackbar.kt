package it.stamp.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import it.stamp.designsystem.icon.CheckCircle
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Gray600
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.designsystem.theme.bodyExtraSmall

sealed interface StampSnackbarVisuals : SnackbarVisuals {
    
    data class Default(
        override val message: String,
        val description: String? = null,
        override val actionLabel: String? = null,
        override val withDismissAction: Boolean = false,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : StampSnackbarVisuals

    data class MissionCompletion(
        val mission: String,
        override val message: String,
        override val actionLabel: String,
        override val withDismissAction: Boolean = false,
        override val duration: SnackbarDuration = SnackbarDuration.Short,
    ) : StampSnackbarVisuals
}

suspend fun SnackbarHostState.displaySnackbar(
    title: String,
    description: String? = null,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult = showSnackbar(
    StampSnackbarVisuals.Default(
        title,
        description,
        actionLabel,
        withDismissAction,
        duration
    )
)

suspend fun SnackbarHostState.displayMissionCompletion(
    mission: String,
    message: String,
    actionLabel: String,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult = showSnackbar(
    StampSnackbarVisuals.MissionCompletion(
        mission,
        message,
        actionLabel,
        withDismissAction,
        duration,
    )
)

@Composable
fun StampSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    when (val visuals = snackbarData.visuals) {
        is StampSnackbarVisuals.Default -> StampDefaultSnackbar(
            title = visuals.message,
            description = visuals.description,
            actionLabel = visuals.actionLabel,
            onActionClick = snackbarData::performAction,
            modifier = modifier,
        )
        is StampSnackbarVisuals.MissionCompletion -> MissionCompletionSnackbar(
            mission = visuals.mission,
            message = visuals.message,
            actionLabel = visuals.actionLabel,
            onActionClick = snackbarData::performAction,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun StampSnackbarPreview() {
    StampTheme {
        Column(
            modifier = Modifier
                .background(White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            StampDefaultSnackbar("미션 조르기가 전달되지 않았어요")

            StampDefaultSnackbar(
                title = "미션 조르기가 전달되지 않았어요",
                description = "다시 시도해주세요",
            )

            StampDefaultSnackbar(
                title = "미션 조르기가 전달되지 않았어요",
                description = "다시 시도해주세요",
                actionLabel = "다시 시도",
            )

            MissionCompletionSnackbar(
                mission = "대청소 함께하기",
                message = "미션을 완료했어요!",
                actionLabel = "취소하기",
                onActionClick = {},
            )
        }
    }
}

@Composable
private fun StampDefaultSnackbar(
    title: String,
    description: String? = null,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
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
                .background(White, CircleShape)
                .border(1.dp, Red400, CircleShape)
                .padding(
                    when {
                        description == null -> PaddingValues(start = 12.dp, end = 16.dp)
                        actionLabel == null -> PaddingValues(start = 12.dp, end = 24.dp)
                        else -> PaddingValues(start = 12.dp, end = 8.dp)
                    }
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .then(
                        if (actionLabel == null) {
                            Modifier
                        } else {
                            Modifier.weight(1F)
                        }
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    imageVector = Drawables.CheckCircle,
                    contentDescription = null,
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = title,
                        color = Gray800,
                        lineHeight = 1.em,
                        style = MaterialTheme.typography.labelSmall,
                    )

                    if (description != null) {
                        Text(
                            text = description,
                            color = Gray600,
                            lineHeight = 1.em,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            if (actionLabel != null) {
                Text(
                    text = actionLabel,
                    modifier = Modifier
                        .clickable(onClick = onActionClick)
                        .padding(12.dp),
                    color = Red400,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 1.em,
                    style = MaterialTheme.typography.bodyExtraSmall,
                )
            }
        }
    }
}

@Composable
private fun MissionCompletionSnackbar(
    mission: String,
    message: String,
    actionLabel: String,
    onActionClick: () -> Unit,
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
                .background(White, CircleShape)
                .border(1.dp, Red400, CircleShape),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier)

            Row(
                modifier = Modifier
                    .weight(1F)
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    imageVector = Drawables.CheckCircle,
                    contentDescription = null,
                )

                ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                    MissionCompletionText(mission, message)
                }
            }

            Text(
                text = actionLabel,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable(onClick = onActionClick)
                    .padding(12.dp),
                color = Red400,
                fontWeight = FontWeight.Medium,
                lineHeight = 1.em,
                style = MaterialTheme.typography.bodyExtraSmall,
            )
        }
    }
}

@Composable
private fun MissionCompletionText(
    mission: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeLayout(modifier) { constraints ->
        val quote = subcompose("prefix") {
            Text("\'")
        }.single()
            .measure(constraints)

        val message = subcompose("suffix") {
            Text("\' $message",)
        }.single()
            .measure(constraints)

        val maxWidth = (constraints.maxWidth - quote.width - message.width)
            .coerceAtLeast(0)

        val title = subcompose("title") {
            Text(
                text = mission,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }.single()
            .measure(constraints.copy(maxWidth = maxWidth))

        val height = maxOf(quote.height, title.height, message.height)

        layout(constraints.maxWidth, height) {
            quote.place(0, 0)

            title.place(quote.width, 0)

            message.place(quote.width + title.width, 0)
        }
    }
}