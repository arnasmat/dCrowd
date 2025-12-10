package com.arnasmat.dcrowd.data.web3

data class Web3Config(
    val rpcUrl: String,
    val contractAddress: String,
    val networkId: String = "1337",
    val chainId: Long = 1337L
)

data class UserCredentials(
    val address: String,
    val privateKey: String,
    val name: String = ""
)

