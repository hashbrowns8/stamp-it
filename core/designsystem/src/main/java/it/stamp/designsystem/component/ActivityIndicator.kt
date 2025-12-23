package it.stamp.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.White

@Composable
fun ActivityIndicator(
    modifier: Modifier = Modifier,
    spokeCount: Int = ActivityIndicatorDefaults.SPOKE_COUNT,
    animatedSpokeCount: Int = ActivityIndicatorDefaults.ANIMATED_SPOKE_COUNT,
    innerRadius: Float = 1 / 3F,
    color: Color = ActivityIndicatorDefaults.color,
    minAlpha: Float = ActivityIndicatorDefaults.MIN_ALPHA,
) {
    require(spokeCount > ActivityIndicatorDefaults.MIN_SPOKE_COUNT)

    require(minAlpha in 0F..1F)

    val unitAngle = 360 / spokeCount

    val infiniteTransition = rememberInfiniteTransition()

    val step by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = spokeCount.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = ActivityIndicatorDefaults.DURATION_MILLIS,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )

    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(48.dp)) {
            val itemWidth = size.width * (1 - innerRadius) / 2

            val itemHeight = size.height / spokeCount

            val itemSize = Size(itemWidth, itemHeight)

            val cornerRadius = itemWidth.coerceAtMost(itemHeight) / 2

            val topLeft = Offset(
                x = size.width - itemWidth,
                y = (size.height - itemHeight) / 2,
            )

            for (angle in 0 until 360 step unitAngle) {
                rotate(angle.toFloat()) {
                    drawRoundRect(
                        color = color.copy(alpha = minAlpha),
                        topLeft = topLeft,
                        size = itemSize,
                        cornerRadius = CornerRadius(cornerRadius),
                    )
                }
            }

            for (i in 1..animatedSpokeCount) {
                rotate((step.toInt() + i) * unitAngle.toFloat()) {
                    drawRoundRect(
                        color = color.copy(alpha = 1F / (i * i)),
                        topLeft = topLeft,
                        size = itemSize,
                        cornerRadius = CornerRadius(cornerRadius),
                    )
                }
            }
        }
    }
}

object ActivityIndicatorDefaults {
    const val MIN_ALPHA: Float = 0.1F
    const val MIN_SPOKE_COUNT: Int = 4
    const val SPOKE_COUNT: Int = 8
    const val ANIMATED_SPOKE_COUNT: Int = 4
    const val DURATION_MILLIS: Int = 1000
    val color: Color = Red400
}

@Preview
@Composable
private fun ActivityIndicatorPreview() {
    Box(
        modifier = Modifier
            .background(White)
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        ActivityIndicator()
    }
}