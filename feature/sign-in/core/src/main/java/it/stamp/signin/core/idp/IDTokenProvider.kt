package it.stamp.signin.core.idp

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import it.stamp.signin.core.BuildConfig

interface IDTokenProvider {
    // TODO : IdentityProvider
    suspend fun getIDToken(activityContext: Context): Result<String>
}

class GoogleIDTokenProvider(
    private val credentialManager: CredentialManager,
) : IDTokenProvider {
    override suspend fun getIDToken(activityContext: Context): Result<String> = runCatching {
        val credentialOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption)
            .build()

        val result = credentialManager.getCredential(activityContext, request)

        val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

        return@runCatching credential.idToken
    }
}