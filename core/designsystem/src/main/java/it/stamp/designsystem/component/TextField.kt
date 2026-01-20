package it.stamp.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TextFieldLabelScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.R
import it.stamp.designsystem.theme.Blue800
import it.stamp.designsystem.theme.Gray100
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.Gray600
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White

@Composable
fun StampTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelPosition: TextFieldLabelPosition = TextFieldLabelPosition.Attached(),
    label: @Composable (TextFieldLabelScope.() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = StampTextFieldDefaults.Shape,
    colors: TextFieldColors = StampTextFieldDefaults.colors(),
    contentPadding: PaddingValues = StampTextFieldDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val border by remember {
        derivedStateOf {
            when {
                isFocused -> colors.focusedIndicatorColor
                state.text.isEmpty() -> null
                else -> colors.unfocusedIndicatorColor
            }?.let { color ->
                BorderStroke(1.dp, color)
            }
        }
    }

    val backgroundColor by remember {
        derivedStateOf {
            if (isFocused) {
                colors.focusedContainerColor
            } else {
                if (state.text.isEmpty()) {
                    colors.unfocusedContainerColor
                } else {
                    colors.focusedContainerColor
                }
            }
        }
    }

    val trailingIcon: (@Composable () -> Unit)? = trailingIcon
        ?: if (lineLimits == TextFieldLineLimits.SingleLine) {
            {
                if (state.text.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .then(
                                if (label != null) {
                                    Modifier.padding(top = 16.dp)
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                state.clearText()
                            }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_cancel),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        } else {
            null
        }

    MaterialTheme(
        typography = with(MaterialTheme.typography) {
            copy(
                bodyLarge = bodyLarge.merge(fontWeight = FontWeight.SemiBold),
                bodySmall = bodySmall.merge(fontSize = 12.sp),
            )
        },
    ) {
        TextField(
            state,
            modifier = modifier
                .clip(shape)
                .background(backgroundColor)
                .then(
                    if (label != null) {
                        Modifier.defaultMinSize(minHeight = 72.dp)
                    } else {
                        Modifier
                    }
                )
                .then(
                    border
                        ?.let { border -> Modifier.border(border, shape) }
                        ?: Modifier
                ),
            enabled,
            readOnly,
            textStyle,
            labelPosition,
            label,
            placeholder,
            leadingIcon,
            trailingIcon,
            prefix,
            suffix,
            supportingText,
            isError,
            inputTransformation,
            outputTransformation,
            keyboardOptions,
            onKeyboardAction,
            lineLimits,
            onTextLayout,
            scrollState,
            shape,
            colors.copy(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            contentPadding,
            interactionSource,
        )
    }

}

object StampTextFieldDefaults {

    val Shape: Shape = RoundedCornerShape(12.dp)

    val ContentPadding: PaddingValues = PaddingValues(
        start = 20.dp,
        top = 12.dp,
        end = 16.dp,
        bottom = 12.dp,
    )

    @Composable
    fun colors(
        focusedTextColor: Color = Gray800,
        unfocusedTextColor: Color = Blue800,
        disabledTextColor: Color = Color.Unspecified,
        errorTextColor: Color = Color.Unspecified,
        focusedContainerColor: Color = White,
        unfocusedContainerColor: Color = Gray25,
        disabledContainerColor: Color = Color.Unspecified,
        errorContainerColor: Color = Color.Unspecified,
        cursorColor: Color = Red400,
        errorCursorColor: Color = cursorColor,
        selectionColors: TextSelectionColors? = TextSelectionColors(
            handleColor = focusedTextColor,
            backgroundColor = focusedTextColor.copy(alpha = 0.4F),
        ),
        focusedIndicatorColor: Color = Gray800,
        unfocusedIndicatorColor: Color = Gray100,
        disabledIndicatorColor: Color = unfocusedIndicatorColor,
        errorIndicatorColor: Color = unfocusedIndicatorColor,
        focusedLabelColor: Color = Gray600,
        unfocusedLabelColor: Color = Gray300,
        disabledLabelColor: Color = Color.Unspecified,
        errorLabelColor: Color = Color.Unspecified,
        focusedPlaceholderColor: Color = Gray300,
        unfocusedPlaceholderColor: Color = Gray300,
        disabledPlaceholderColor: Color = unfocusedPlaceholderColor,
        errorPlaceholderColor: Color = disabledPlaceholderColor,
    ): TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = errorTextColor,
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = errorContainerColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        selectionColors = selectionColors,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = errorPlaceholderColor,
    )
}

@Preview
@Composable
private fun StampTextFieldPreview() {
    StampTheme {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .background(White)
                .padding(24.dp)
                .clickable {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val state = rememberTextFieldState()

            StampTextField(
                state,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("입력해야 할 정보")
                },
                lineLimits = TextFieldLineLimits.SingleLine,
            )
        }
    }
}