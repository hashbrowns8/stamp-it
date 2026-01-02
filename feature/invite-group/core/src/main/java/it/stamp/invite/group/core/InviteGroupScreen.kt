package it.stamp.invite.group.core

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.stamp.designsystem.icon.ArrowLeft
import it.stamp.designsystem.icon.CharacterRed
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.icon.Export
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray25
import it.stamp.designsystem.theme.Gray500
import it.stamp.designsystem.theme.Gray800
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.domain.generator.InviteCodeGenerator
import it.stamp.model.membership.InviteCode
import it.stamp.ui.StampTopAppBar

@Composable
internal fun InviteGroupScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InviteGroupViewModel = hiltViewModel(),
) {
    val inviteCode by viewModel.inviteCode.collectAsStateWithLifecycle()

    InviteGroupScreen(inviteCode, onBackClick, modifier)
}

@Composable
private fun InviteGroupScreen(
    inviteCode: InviteCode,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier, color = White) {
        StampTopAppBar(
            title = {
                Text(stringResource(R.string.invite))
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Drawables.ArrowLeft,
                        contentDescription = null,
                    )
                }
            }
        )

        Column(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Bottom),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box {
                    Canvas(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(top = 56.dp)
                            .width(100.dp)
                            .height(50.dp)
                    ) {
                        drawOval(color = Black.copy(alpha = 0.1F))
                    }

                    Image(
                        painter = Drawables.CharacterRed,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .width(64.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }

                Text(
                    text = stringResource(R.string.invite_code_description),
                    color = Gray800,
                    style = MaterialTheme.typography.bodyMedium,
                )

                val context = LocalContext.current

                val share by rememberUpdatedState {
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND)
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_TEXT, inviteCode.value),
                        null
                    ).let(context::startActivity)
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip( RoundedCornerShape(12.dp))
                        .background(Gray25)
                        .clickable(onClick = share)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.invite_code),
                        color = Gray800,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = inviteCode.value,
                        modifier = Modifier.weight(1F),
                        color = Gray800,
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Icon(
                        imageVector = Drawables.Export,
                        contentDescription = null,
                        tint = Gray500,
                    )
                }
            }

            Box(Modifier.weight(1F))
        }
    }
}

@Preview
@Composable
private fun InviteGroupScreenPreview() {
    StampTheme {
        val inviteCode = remember {
            InviteCodeGenerator.generate()
        }

        InviteGroupScreen(
            inviteCode,
            onBackClick = {
            },
        )
    }
}