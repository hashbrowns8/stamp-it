package it.stamp.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import it.stamp.designsystem.theme.Gray50
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.Red400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White

@Composable
fun StampAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirm: String,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    dismiss: String? = null,
    onDismissClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(28.dp),
    containerColor: Color = White,
    dismissContentColor: Color = Gray800,
    confirmContentColor: Color = Red400,
    titleContentColor: Color = Gray800,
    textContentColor: Color = Gray800,
    properties: DialogProperties = DialogProperties(),
) {
    BasicAlertDialog(
        onDismissRequest,
        modifier,
        properties,
    ) {
        Surface(
            shape = shape,
            color = containerColor,
        ) {
            val textStyle = MaterialTheme.typography.labelLarge.merge(fontSize = 17.sp)

            ProvideTextStyle(textStyle) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = title,
                            color = titleContentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            text,
                            color = textContentColor,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 1.4.em,
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf(
                            Triple(dismiss, dismissContentColor, onDismissClick),
                            Triple(confirm, confirmContentColor, onConfirmClick),
                        ).forEach { (text, color, onClick) ->
                            if (text != null && onClick != null) {
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(48.dp)
                                        .clip(CircleShape)
                                        .background(Gray50)
                                        .clickable(onClick = onClick),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text,
                                        color = color,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AlertDialogPreview() {
    StampTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {
            StampAlertDialog(
                onDismissRequest = {},
                title = "정말 로그아웃 하시겠어요?",
                text = "현재까지의 모든 데이터는\n재로그인할 때까지 안전하게 보관돼요",
                dismiss = "취소",
                onDismissClick = {},
                confirm = "확인",
                onConfirmClick = {},
            )
        }
    }
}