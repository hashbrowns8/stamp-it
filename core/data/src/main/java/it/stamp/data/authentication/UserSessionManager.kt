package it.stamp.data.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import it.stamp.model.ids.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface UserSessionManager {
    val currentUserId: Flow<UserId?>

    suspend fun setUserId(id: UserId)

    suspend fun clearSession()
}

@Singleton
class DataStoreUserSessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserSessionManager {

    override val currentUserId: Flow<UserId?> = dataStore.data
        .map { preferences ->
            preferences[USER_ID]?.let(::UserId)
        }

    override suspend fun setUserId(id: UserId) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = id.value
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
        }
    }

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
    }
}