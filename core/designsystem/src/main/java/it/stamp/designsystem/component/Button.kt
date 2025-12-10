package it.stamp.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.Red50
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White

enum class ButtonSize {
    Large,
    Medium,
    Small;
}

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonDefaults.Size,
    shape: Shape = ButtonDefaults.shape(size),
    colors: ButtonColors = ButtonDefaults.primaryButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(size),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        enabled,
        size,
        shape,
        colors,
        null,
        contentPadding,
        interactionSource,
        modifier,
        content,
    )
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonDefaults.Size,
    shape: Shape = ButtonDefaults.shape(size),
    colors: ButtonColors = ButtonDefaults.secondaryButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(size),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        enabled,
        size,
        shape,
        colors,
        null,
        contentPadding,
        interactionSource,
        modifier,
        content,
    )
}

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: ButtonSize = ButtonDefaults.Size,
    shape: Shape = ButtonDefaults.shape(size),
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke = ButtonDefaults.outlinedButtonBorder(enabled),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(size),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick,
        enabled,
        size,
        shape,
        colors,
        border,
        contentPadding,
        interactionSource,
        modifier,
        content,
    )
}

@Composable
private fun Button(
    onClick: () -> Unit,
    enabled: Boolean = true,
    size: ButtonSize,
    shape: Shape,
    colors: ButtonColors,
    border: BorderStroke?,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val (containerColor, contentColor) = with(colors) {
        if (enabled) {
            containerColor to contentColor
        } else {
            disabledContainerColor to disabledContentColor
        }
    }

    val textStyle = ButtonDefaults.textStyle(size)

    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
        interactionSource = interactionSource,
    ) {
        val mergedStyle = LocalTextStyle.current.merge(textStyle)

        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides mergedStyle,
        ) {
            Row(
                modifier = Modifier.padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content,
            )
        }
    }
}

@Preview
@Composable
private fun ButtonPreview() {
    StampItTheme {
        Column(
            modifier = Modifier
                .background(White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            listOf(true, false).forEach { enabled ->
                PrimaryButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    size = ButtonSize.Large,
                ) {
                    Text("버튼")
                }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    size = ButtonSize.Large,
                ) {
                    Text("버튼")
                }

                SecondaryButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    size = ButtonSize.Large,
                ) {
                    Text("버튼")
                }
            }
        }
    }
}

object ButtonDefaults {

    val Size: ButtonSize = ButtonSize.Large

    @Composable
    fun textStyle(
        size: ButtonSize
    ): TextStyle = when (size) {
        ButtonSize.Small -> MaterialTheme.typography.labelSmall
        ButtonSize.Medium -> MaterialTheme.typography.labelMedium
        ButtonSize.Large -> MaterialTheme.typography.labelLarge
    }

    fun contentPadding(
        size: ButtonSize
    ): PaddingValues = when (size) {
        ButtonSize.Small -> PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ButtonSize.Medium -> PaddingValues(horizontal = 72.dp, vertical = 12.dp)
        ButtonSize.Large -> PaddingValues(vertical = 16.dp)
    }

    fun shape(size: ButtonSize) = when (size) {
        ButtonSize.Small -> RoundedCornerShape(8.dp)
        ButtonSize.Medium -> RoundedCornerShape(10.dp)
        ButtonSize.Large -> RoundedCornerShape(12.dp)
    }

    fun primaryButtonColors(
        containerColor: Color = Red400,
        contentColor: Color = White,
        disabledContainerColor: Color = Gray300,
        disabledContentColor: Color = White,
    ): ButtonColors = ButtonColors(
        containerColor,
        contentColor,
        disabledContainerColor,
        disabledContentColor,
    )

    fun secondaryButtonColors(
        containerColor: Color = Red50,
        contentColor: Color = Red400,
        disabledContainerColor: Color = Gray50,
        disabledContentColor: Color = Gray300,
    ): ButtonColors = ButtonColors(
        containerColor,
        contentColor,
        disabledContainerColor,
        disabledContentColor,
    )

    fun outlinedButtonColors(
        containerColor: Color = White,
        contentColor: Color = Red400,
        disabledContainerColor: Color = Gray50,
        disabledContentColor: Color = Gray500,
    ): ButtonColors = ButtonColors(
        containerColor,
        contentColor,
        disabledContainerColor,
        disabledContentColor,
    )

    fun outlinedButtonBorder(enabled: Boolean = true): BorderStroke = BorderStroke(
        width = 1.dp,
        color = if (enabled) {
            Red400
        } else {
            Gray500
        },
    )
}