package it.stamp.signin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SignInWithButton(
    logo: Painter,
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    borderStroke: BorderStroke? = null,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .then(
                if (borderStroke != null) {
                    Modifier.border(borderStroke, RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
                .size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(painter = logo, contentDescription = null)
        }

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            color = contentColor,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}