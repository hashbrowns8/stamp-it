package it.stamp.signin

import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption

internal fun buildGetCredentialRequest(): GetCredentialRequest {
    val credentialOption = GetGoogleIdOption.Builder()
        .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(true)
        .build()

    return GetCredentialRequest.Builder()
        .addCredentialOption(credentialOption)
        .build()
}