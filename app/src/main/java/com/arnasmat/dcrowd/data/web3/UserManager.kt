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

    val currentUserFlow: Flow<UserCredentials?> = context.dataStore.data.map { preferences ->
        val address = preferences[PreferenceKeys.CURRENT_USER_ADDRESS]
        val privateKey = preferences[PreferenceKeys.CURRENT_USER_PRIVATE_KEY]
        val name = preferences[PreferenceKeys.CURRENT_USER_NAME]

        if (address != null && privateKey != null) {
            UserCredentials(
                address = address,
                privateKey = privateKey,
                name = name ?: ""
            )
        } else {
            null
        }
    }

    suspend fun getCurrentUser(): UserCredentials? {
        return currentUserFlow.first()
    }

    suspend fun loginUser(address: String, privateKey: String, name: String = "") {
        if (!isValidAddress(address)) {
            throw IllegalArgumentException("Invalid Ethereum address format")
        }
        if (!isValidPrivateKey(privateKey)) {
            throw IllegalArgumentException("Invalid private key format")
        }

        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.CURRENT_USER_ADDRESS] = address
            preferences[PreferenceKeys.CURRENT_USER_PRIVATE_KEY] = privateKey
            preferences[PreferenceKeys.CURRENT_USER_NAME] = name
        }
    }

    suspend fun clearCurrentUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.CURRENT_USER_ADDRESS)
            preferences.remove(PreferenceKeys.CURRENT_USER_PRIVATE_KEY)
            preferences.remove(PreferenceKeys.CURRENT_USER_NAME)
        }
    }

    private fun isValidAddress(address: String): Boolean {
        return address.matches(Regex("^0x[0-9a-fA-F]{40}$"))
    }

    private fun isValidPrivateKey(privateKey: String): Boolean {
        val cleanKey = if (privateKey.startsWith("0x")) {
            privateKey.substring(2)
        } else {
            privateKey
        }
        return cleanKey.matches(Regex("^[0-9a-fA-F]{64}$"))
    }
}

