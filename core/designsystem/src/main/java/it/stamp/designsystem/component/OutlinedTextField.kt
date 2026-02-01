package it.stamp.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.stamp.designsystem.R
import it.stamp.designsystem.theme.Gray100
import it.stamp.designsystem.theme.Gray200
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray300
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White

@Composable
fun StampOutlinedTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography
        .bodyLarge
        .merge(fontWeight = FontWeight.SemiBold),
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
    lineLimits: TextFieldLineLimits = StampOutlinedTextFieldDefaults.lineLimits,
    onTextLayout: (Density.(getResult: () -> TextLayoutResult?) -> Unit)? = null,
    scrollState: ScrollState = rememberScrollState(),
    shape: Shape = StampOutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = StampOutlinedTextFieldDefaults.colors(),
    contentPadding: PaddingValues = StampOutlinedTextFieldDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val border by remember {
        derivedStateOf {
            with(colors) {
                BorderStroke(
                    1.dp,
                    if (isFocused) {
                        focusedIndicatorColor
                    } else {
                        unfocusedIndicatorColor
                    },
                )
            }
        }
    }

    val trailingIcon: (@Composable () -> Unit)? = trailingIcon
        ?: if (lineLimits == TextFieldLineLimits.SingleLine) {
            {
                if (isFocused and state.text.isNotEmpty()) {
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
        OutlinedTextField(
            state = state,
            modifier = modifier
                .border(border, shape)
                .defaultMinSize(minHeight = 64.dp),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            labelPosition = labelPosition,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            lineLimits = lineLimits,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            scrollState = scrollState,
            shape = shape,
            colors = colors.copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            contentPadding = contentPadding,
        )
    }
}

object StampOutlinedTextFieldDefaults {
    val lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine

    val shape: Shape = RoundedCornerShape(12.dp)

    @Composable
    fun colors(
        focusedTextColor: Color = Gray800,
        unfocusedTextColor: Color = Gray400,
        disabledTextColor: Color = Gray300,
        errorTextColor: Color = disabledTextColor,
        focusedContainerColor: Color = White,
        unfocusedContainerColor: Color = White,
        disabledContainerColor: Color = Gray25,
        errorContainerColor: Color = disabledContainerColor,
        cursorColor: Color = Red400,
        errorCursorColor: Color = cursorColor,
        selectionColors: TextSelectionColors? = TextSelectionColors(
            handleColor = cursorColor,
            backgroundColor = cursorColor.copy(alpha = 0.4F),
        ),
        focusedBorderColor: Color = Gray800,
        unfocusedBorderColor: Color = Gray200,
        disabledBorderColor: Color = unfocusedBorderColor,
        errorBorderColor: Color = unfocusedBorderColor,
        focusedLabelColor: Color = Color.Unspecified,
        unfocusedLabelColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
        errorLabelColor: Color = Color.Unspecified,
        focusedPlaceholderColor: Color = Gray100,
        unfocusedPlaceholderColor: Color = Gray100,
        disabledPlaceholderColor: Color = unfocusedPlaceholderColor,
        errorPlaceholderColor: Color = disabledPlaceholderColor,
    ) = OutlinedTextFieldDefaults.colors(
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
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        disabledBorderColor = disabledBorderColor,
        errorBorderColor = errorBorderColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = errorPlaceholderColor,
    )

    fun contentPadding(
        start: Dp = 20.dp,
        top: Dp = 12.dp,
        end: Dp = 16.dp,
        bottom: Dp = 12.dp,
    ): PaddingValues = PaddingValues(start, top, end, bottom)
}

@Preview
@Composable
private fun OutlinedTextFieldPreview() {
    StampTheme {
        Box(
            modifier = Modifier
                .background(White)
                .padding(24.dp),
        ) {
            val state = rememberTextFieldState("활발한토끼")

            StampOutlinedTextField(
                state,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("닉네임")
                },
            )
        }
    }
}