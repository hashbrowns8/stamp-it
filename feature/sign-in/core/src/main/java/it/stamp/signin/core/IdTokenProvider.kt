package it.stamp.signin.core

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface IdTokenProvider {
    suspend fun getIdToken(activityContext: Context): Result<String>
}

class GoogleIdTokenProvider(
    private val credentialManager: CredentialManager,
) : IdTokenProvider {
    override suspend fun getIdToken(activityContext: Context): Result<String> = runCatching {
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