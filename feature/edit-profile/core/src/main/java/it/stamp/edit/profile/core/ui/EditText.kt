package it.stamp.edit.profile.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.component.StampOutlinedTextField

@Composable
internal fun EditText(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: TextFieldState = rememberTextFieldState(value),
    inputTransformation: InputTransformation? = null,
) {
    LaunchedEffect(state) {
        snapshotFlow { state.text }
            .collect { onValueChange(it.toString()) }
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Label(label)

        StampOutlinedTextField(
            state,
            modifier = Modifier.fillMaxWidth(),
            enabled,
            inputTransformation = inputTransformation,
        )
    }
}