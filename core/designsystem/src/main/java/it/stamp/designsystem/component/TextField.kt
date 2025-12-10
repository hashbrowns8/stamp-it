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
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White
import androidx.compose.material3.TextField as MaterialTextField
import androidx.compose.material3.TextFieldDefaults as MaterialTextFieldDefaults

@Composable
fun TextField(
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
    shape: Shape = TextFieldDefaults.Shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    contentPadding: PaddingValues = TextFieldDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderStroke by remember {
        derivedStateOf {
            if (isFocused) {
                BorderStroke(1.dp, Gray800)
            } else {
                if (state.text.isEmpty()) {
                    null
                } else {
                    BorderStroke(1.dp, Gray100)
                }
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
                            .padding(top = 16.dp)
                            .size(24.dp)
                            .clickable {
                                state.clearText()
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_cancel),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )
                    }
                }
            }
        } else {
            null
        }

    MaterialTheme(
        typography = MaterialTheme.typography.copy(
            bodyLarge = MaterialTheme.typography.bodyLarge.merge(
                color = Gray300,
                fontWeight = FontWeight.SemiBold
            ),
            bodySmall = MaterialTheme.typography.bodySmall.merge(
                color = Gray600,
                fontSize = 12.sp,
            )
        )
    ) {
        MaterialTextField(
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
                .then(borderStroke?.let { Modifier.border(it, shape) } ?: Modifier),
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
            ),
            contentPadding,
            interactionSource,
        )
    }

}

object TextFieldDefaults {

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
        cursorColor: Color = Gray800,
        errorCursorColor: Color = Color.Unspecified,
        focusedIndicatorColor: Color = Color.Transparent,
        unfocusedIndicatorColor: Color = Color.Transparent,
        disabledIndicatorColor: Color = Color.Transparent,
        errorIndicatorColor: Color = Color.Transparent,
        focusedLabelColor: Color = Gray600,
        unfocusedLabelColor: Color = Gray300,
        disabledLabelColor: Color = Color.Unspecified,
        errorLabelColor: Color = Color.Unspecified,
    ): TextFieldColors = MaterialTextFieldDefaults.colors(
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
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
    )
}

@Preview
@Composable
private fun TextFieldPreview() {
    StampItTheme {
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

            TextField(
                state,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("입력해야 할 정보")
                },
                lineLimits = TextFieldLineLimits.SingleLine,
            )

            TextField(
                state = rememberTextFieldState(),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}