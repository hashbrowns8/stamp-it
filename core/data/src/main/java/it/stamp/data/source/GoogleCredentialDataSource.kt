package it.stamp.data.source

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleCredentialDataSource @Inject constructor(
    private val context: Context,
) {
    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    suspend fun getGoogleIdToken(): String {
        val credentialOption = GetGoogleIdOption.Builder()
            .setServerClientId("805353772104-hvetaseshrb80kbnn2p1ebf9mkcfuhh3.apps.googleusercontent.com")
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption)
            .build()

        val response = credentialManager.getCredential(context, request)

        val credential = response.credential as? GoogleIdTokenCredential
            ?: throw IllegalStateException("Invalid Credential Type :(")

        return credential.idToken
    }
}