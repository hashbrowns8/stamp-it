package it.stamp.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import it.stamp.designsystem.R

object Icons

val Icons.Cancel: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.icon_cancel)

val Icons.Logo: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo)

val Icons.LogoInversed: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_inversed)

val Icons.LogoOutlined: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.logo_outlined)