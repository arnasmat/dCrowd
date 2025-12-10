package com.arnasmat.dcrowd.data.web3

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.ContractGasProvider
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Web3Factory @Inject constructor(
    private val web3ConfigManager: Web3ConfigManager
) {
    suspend fun createWeb3j(): Web3j {
        val config = web3ConfigManager.getConfig()
            ?: throw IllegalStateException("Web3 configuration not set. Please configure RPC URL in settings.")
        return Web3j.build(HttpService(config.rpcUrl))
    }

    fun createWeb3j(rpcUrl: String): Web3j {
        return Web3j.build(HttpService(rpcUrl))
    }

    fun createCredentials(privateKey: String): Credentials {
        val cleanPrivateKey = if (privateKey.startsWith("0x")) {
            privateKey.substring(2)
        } else {
            privateKey
        }
        return Credentials.create(cleanPrivateKey)
    }

    fun createCredentials(user: UserCredentials): Credentials {
        return createCredentials(user.privateKey)
    }

    fun getGasProvider(): ContractGasProvider {
        return createCustomGasProvider()
    }

    fun createCustomGasProvider(
        gasPrice: BigInteger = BigInteger.valueOf(20_000_000_000L),
        gasLimit: BigInteger = BigInteger.valueOf(6_721_975L)
    ): ContractGasProvider {
        return object : ContractGasProvider {
            override fun getGasPrice(): BigInteger = gasPrice

            override fun getGasLimit(): BigInteger = gasLimit

            override fun getGasLimit(transaction: org.web3j.protocol.core.methods.request.Transaction?): BigInteger = gasLimit
        }
    }
}

