package com.arnasmat.dcrowd.data.repository

import android.content.Context
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider

class EthereumRepository(context: Context) {

    private val DEFAULT_ADDRESS = "http://10.0.2.2:7545" // emulator -> ganache localhosted

    private val web3j = Web3j.build(HttpService(DEFAULT_ADDRESS))

    // Load user's credentials from private key
    private fun getCredentials(privateKeyHex: String): Credentials {
        // Remove "0x" prefix if present
        val cleanKey = privateKeyHex.removePrefix("0x")
        return Credentials.create(cleanKey)
    }

    // Gas provider for transactions
    private val gasProvider = DefaultGasProvider()
}