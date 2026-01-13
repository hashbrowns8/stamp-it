package it.stamp.join.group.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DataLossConsentDialog(
    onDismissRequest: () -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 그룹 이동

    // 이동 후 복구는 불가능하며 그룹에서
    // 생성된 스티커와 미션이 모두 삭제됩니다

    // 취소 | 입장하기
}