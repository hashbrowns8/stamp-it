package it.stamp.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.theme.White

@Composable
fun StampModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = StampBottomSheetDefaults.Shape,
    containerColor: Color = StampBottomSheetDefaults.ContainerColor,
    dragHandle: @Composable () -> Unit = {
        StampBottomSheetDefaults.DragHandle()
    },
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest,
        modifier,
        sheetState,
        shape = shape,
        containerColor = containerColor,
        dragHandle = dragHandle,
        content = content,
    )
}

data object StampBottomSheetDefaults {
    val Shape: Shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)

    val ContainerColor: Color = White

    @Composable
    fun DragHandle(
        modifier: Modifier = Modifier,
        width: Dp = 56.dp,
        height: Dp = 6.dp,
        shape: Shape = CircleShape,
        color: Color = Color(0xFFD9D9D9),
    ) {
        Surface(
            modifier = modifier.padding(vertical = 16.dp),
            color = color,
            shape = shape,
        ) {
            Box(Modifier.size(width, height))
        }
    }
}