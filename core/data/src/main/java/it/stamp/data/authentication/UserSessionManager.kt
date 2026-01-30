package it.stamp.data.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.stamp.model.authentication.IdentityProvider
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class UserSession(val userId: UserId, val identityProvider: IdentityProvider)

interface UserSessionManager {
    val session: Flow<UserSession?>

    suspend fun saveSession(session: UserSession)

    suspend fun clearSession()
}

@Singleton
class DataStoreUserSessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserSessionManager {

    override val session: Flow<UserSession?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID]
                ?.let(::UserId)
                ?.let { userId ->
                    preferences[IDENTITY_PROVIDER]
                        ?.let(IdentityProvider::valueOf)
                        ?.let { identityProvider ->
                            UserSession(userId, identityProvider)
                        }
                }
        }

    override suspend fun saveSession(session: UserSession) {
        dataStore.edit { preferences ->
            with(session) {
                preferences[USER_ID] = userId.value
                preferences[IDENTITY_PROVIDER] = identityProvider.name
            }
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(IDENTITY_PROVIDER)
        }
    }

    companion object {
        private val USER_ID = stringPreferencesKey("USER_ID")
        private val IDENTITY_PROVIDER = stringPreferencesKey("IDENTITY_PROVIDER")
    }
}