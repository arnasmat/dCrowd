package com.arnasmat.dcrowd.data.web3

import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Web3Factory @Inject constructor(
    private val ganacheConfig: GanacheConfig
) {
    fun createWeb3j(): Web3j {
        return Web3j.build(HttpService(ganacheConfig.rpcUrl))
    }

    fun createCredentials(privateKey: String): Credentials {
        // Remove "0x" prefix if present
        val cleanPrivateKey = if (privateKey.startsWith("0x")) {
            privateKey.substring(2)
        } else {
            privateKey
        }
        return Credentials.create(cleanPrivateKey)
    }

    fun createCredentials(user: GanacheUser): Credentials {
        return createCredentials(user.privateKey)
    }

    fun getGasProvider(): DefaultGasProvider {
        return DefaultGasProvider()
    }

    fun createCustomGasProvider(
        gasPrice: BigInteger = BigInteger.valueOf(20_000_000_000L), // 20 Gwei
        gasLimit: BigInteger = BigInteger.valueOf(6_721_975L) // Ganache default
    ): org.web3j.tx.gas.ContractGasProvider {
        return object : org.web3j.tx.gas.ContractGasProvider {
            override fun getGasPrice(): BigInteger = gasPrice

            override fun getGasLimit(): BigInteger = gasLimit

            override fun getGasLimit(transaction: org.web3j.protocol.core.methods.request.Transaction?): BigInteger = gasLimit
        }
    }
}

