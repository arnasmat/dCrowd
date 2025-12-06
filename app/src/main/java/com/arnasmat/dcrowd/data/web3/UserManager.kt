package com.arnasmat.dcrowd.data.web3

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    private object PreferenceKeys {
        val CURRENT_USER_ADDRESS = stringPreferencesKey("current_user_address")
        val CURRENT_USER_PRIVATE_KEY = stringPreferencesKey("current_user_private_key")
        val CURRENT_USER_NAME = stringPreferencesKey("current_user_name")
    }

    val currentUserFlow: Flow<GanacheUser?> = context.dataStore.data.map { preferences ->
        val address = preferences[PreferenceKeys.CURRENT_USER_ADDRESS]
        val privateKey = preferences[PreferenceKeys.CURRENT_USER_PRIVATE_KEY]
        val name = preferences[PreferenceKeys.CURRENT_USER_NAME]

        if (address != null && privateKey != null) {
            GanacheUser(
                address = address,
                privateKey = privateKey,
                name = name ?: ""
            )
        } else {
            null
        }
    }

    suspend fun getCurrentUser(): GanacheUser? {
        return currentUserFlow.first()
    }

    suspend fun switchUser(user: GanacheUser) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_USER_ADDRESS] = user.address
            preferences[PreferenceKeys.CURRENT_USER_PRIVATE_KEY] = user.privateKey
            preferences[PreferenceKeys.CURRENT_USER_NAME] = user.name
        }
    }

    suspend fun isCurrentUserSystemOwner(): Boolean {
        val currentUser = getCurrentUser() ?: return false
        return currentUser.address.equals(GanacheAccounts.SYSTEM_OWNER.address, ignoreCase = true)
    }

    suspend fun clearCurrentUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.CURRENT_USER_ADDRESS)
            preferences.remove(PreferenceKeys.CURRENT_USER_PRIVATE_KEY)
            preferences.remove(PreferenceKeys.CURRENT_USER_NAME)
        }
    }

    fun getAvailableUsers(excludeSystemOwner: Boolean = false): List<GanacheUser> {
        return if (excludeSystemOwner) {
            GanacheAccounts.DEFAULT_ACCOUNTS.filter {
                !it.address.equals(GanacheAccounts.SYSTEM_OWNER.address, ignoreCase = true)
            }
        } else {
            GanacheAccounts.DEFAULT_ACCOUNTS
        }
    }

    suspend fun initializeDefaultUser() {
        if (getCurrentUser() == null) {
            switchUser(GanacheAccounts.INIT_USER)
        }
    }
}

