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
class Web3ConfigManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "web3_config")

    private object PreferenceKeys {
        val RPC_URL = stringPreferencesKey("rpc_url")
        val CONTRACT_ADDRESS = stringPreferencesKey("contract_address")
        val NETWORK_ID = stringPreferencesKey("network_id")
        val CHAIN_ID = stringPreferencesKey("chain_id")
    }

    val configFlow: Flow<Web3Config?> = context.dataStore.data.map { preferences ->
        val rpcUrl = preferences[PreferenceKeys.RPC_URL]
        val contractAddress = preferences[PreferenceKeys.CONTRACT_ADDRESS]

        if (rpcUrl != null && contractAddress != null) {
            Web3Config(
                rpcUrl = rpcUrl,
                contractAddress = contractAddress,
                networkId = preferences[PreferenceKeys.NETWORK_ID] ?: "1337",
                chainId = preferences[PreferenceKeys.CHAIN_ID]?.toLongOrNull() ?: 1337L
            )
        } else {
            null
        }
    }

    suspend fun getConfig(): Web3Config? {
        return configFlow.first()
    }

    suspend fun saveConfig(config: Web3Config) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.RPC_URL] = config.rpcUrl
            preferences[PreferenceKeys.CONTRACT_ADDRESS] = config.contractAddress
            preferences[PreferenceKeys.NETWORK_ID] = config.networkId
            preferences[PreferenceKeys.CHAIN_ID] = config.chainId.toString()
        }
    }

    suspend fun clearConfig() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.RPC_URL)
            preferences.remove(PreferenceKeys.CONTRACT_ADDRESS)
            preferences.remove(PreferenceKeys.NETWORK_ID)
            preferences.remove(PreferenceKeys.CHAIN_ID)
        }
    }
}

