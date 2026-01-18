package it.stamp.signin.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import it.stamp.designsystem.icon.Drawables
import it.stamp.designsystem.theme.Black
import it.stamp.designsystem.theme.Gray400
import it.stamp.designsystem.theme.StampTheme
import it.stamp.designsystem.theme.White
import it.stamp.signin.core.idp.GoogleIDTokenProvider
import it.stamp.signin.core.idp.IDTokenProvider
import it.stamp.signin.core.ui.SignInWithButton
import kotlinx.coroutines.launch

@Composable
internal fun SignInScreen(
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is SignInUiEvent.SignedIn -> onSignInSuccess()
                is SignInUiEvent.SignInFailed -> {

                }
            }
        }
    }

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val idTokenProvider: IDTokenProvider = remember(context) {
        GoogleIDTokenProvider(CredentialManager.create(context))
    }

    SignInScreen(
        onSignInWithAppleClick = {},
        onSignInWithGoogleClick = {
            coroutineScope.launch {
                idTokenProvider.getIDToken(context)
                    .onSuccess { idToken ->
                        viewModel.signInWithGoogle(idToken)
                    }
                    .onFailure { throwable ->
                        if (throwable is GetCredentialCancellationException) return@onFailure
                    }
            }
        },
        onSignInWithKakaoClick = {},
        modifier,
    )
}

@Composable
private fun SignInScreen(
    onSignInWithAppleClick: () -> Unit,
    onSignInWithGoogleClick: () -> Unit,
    onSignInWithKakaoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White),
            verticalArrangement = Arrangement.spacedBy(120.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    imageVector = Drawables.Logo,
                    contentDescription = null,
                    modifier = Modifier.height(56.dp),
                    contentScale = ContentScale.FillHeight,
                )

                Text(
                    text = stringResource(R.string.tagline),
                    color = Gray400,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SignInWithButton(
                    logo = painterResource(R.drawable.icon_apple),
                    text = stringResource(R.string.sign_in_with_apple),
                    containerColor = Black,
                    contentColor = White,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignInWithAppleClick,
                )

                SignInWithButton(
                    logo = painterResource(R.drawable.icon_google),
                    text = stringResource(R.string.sign_in_with_google),
                    containerColor = White,
                    contentColor = Black,
                    borderStroke = BorderStroke(1.dp, Black),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignInWithGoogleClick,
                )

                SignInWithButton(
                    logo = painterResource(R.drawable.icon_kakao),
                    text = stringResource(R.string.sign_in_with_kakao),
                    containerColor = Color(0xFFFEE500),
                    contentColor = Black,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSignInWithKakaoClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    StampTheme {
        SignInScreen(
            onSignInWithAppleClick = {},
            onSignInWithGoogleClick = {},
            onSignInWithKakaoClick = {},
        )
    }
}