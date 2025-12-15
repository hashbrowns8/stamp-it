package it.stamp.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.stamp.designsystem.icon.ArrowLeft
import it.stamp.designsystem.icon.Icons
import it.stamp.designsystem.icon.Plus
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampItTheme
import it.stamp.designsystem.theme.White
import androidx.compose.material3.TopAppBar as MaterialTopAppBar

@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
) {
    MaterialTopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
    )
}

object TopAppBarDefaults {

    fun topAppBarColors(
        containerColor: Color = White,
        navigationIconContentColor: Color = Gray800,
        titleContentColor: Color = Gray800,
        actionIconContentColor: Color = Gray500,
    ): TopAppBarColors = TopAppBarColors(
        containerColor,
        scrolledContainerColor = Color.Unspecified,
        navigationIconContentColor,
        titleContentColor,
        actionIconContentColor,
        subtitleContentColor = Color.Unspecified,
    )
}

@Preview
@Composable
private fun TopAppBarPreview() {
    StampItTheme {
        TopAppBar(
            title = {
                Row {
                    Spacer(Modifier.width(8.dp))

                    Text("미션 전달하기")
                }
            },
            navigationIcon = {
                Row {
                    Spacer(Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.ArrowLeft,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                Icon(
                    imageVector = Icons.Plus,
                    contentDescription = null,
                )

                Spacer(Modifier.width(12.dp))
            },
        )
    }
}