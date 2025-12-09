package com.arnasmat.dcrowd.data.web3

const val BASE_URL = "http://10.0.2.2:8545" // Android emu -> localhost
const val CONTRACT_ADDRESS = "0xFe4F2B55064De1317597BaC58f553306370eD8Fa"

data class GanacheConfig(
    val rpcUrl: String = BASE_URL, // Android emulator to localhost
    val networkId: String = "1337", // Ganache default network ID
    val chainId: Long = 1337L,
    val contractAddress: String = CONTRACT_ADDRESS
)

data class GanacheUser(
    val address: String,
    val privateKey: String,
    val balance: String = "0",
    val name: String = ""
)

// TODO! Change this each time ganache-cli is run.
object GanacheAccounts {
    val DEFAULT_ACCOUNTS = listOf(
        GanacheUser(
            address = "0x5Da2e4EfCb05886CE271f0c2955F738E3DC7726D",
            privateKey = "0xefaeda48e28724abb14b7d5cd3c6d12595d7966bdedaa290aa54b5e375a8f74e",
            name = "sysOwner"
        ),
        GanacheUser(
            address = "0x6ba27592Dcc292eBCec39ad1BCB35315Ab7862cc",
            privateKey = "0xd4407949cd0a4fced2f387b6d74eacefcd1c97113b58554b9bea451ecfe72e88",
            name = "U1"
        ),
        GanacheUser(
            address = "0xADEF3AC75146814e25b9BAE5c46674Ec919B079D",
            privateKey = "0xca383aba73a62f7c0346b24b9aa58d72946f5b7571dba8d6c7d43e81515be557",
            name = "U2"
        ),
        GanacheUser(
            address = "0x7EDee92149f87991cA37662d4801f2Ec42A478F1",
            privateKey = "0x6edc94b0dcc935ec77949cf54f1d6dc95476cdcbf283ad36b4b8e65625487d39",
            name = "U3"
        ),
        GanacheUser(
            address = "0x9a31305248f8299672F55FbED8f7E244084f38A6",
            privateKey = "0xf99faba58aa68de560bf73cfa65d4203a38e31b7fe5ac409383e5565642b8868",
            name = "U4"
        ),
        GanacheUser(
            address = "0x4a194E4be25a4cCF59A9CCa556263a063256F428",
            privateKey = "0x10d8c1e3ae963d0286ce02255aec25a87fddbbb8f17a15fea422f65233681878",
            name = "U5"
        ),
        GanacheUser(
            address = "0x3E5e9111Ae8eB78Fe1CC3bb8915d5D461F3Ef9A9",
            privateKey = "0xe485d098507f54e7733a205420dfddbe58db035fa577fc294ebd14db90767a52",
            name = "U6"
        ),
        GanacheUser(
            address = "0x28a8746e75304c0780E011BEd21C72cD78cd535E",
            privateKey = "0xa453611d9419d0e56f499079478fd72c37b251a94bfde4d19872c44cf65386e3",
            name = "U7"
        ),
        GanacheUser(
            address = "0xACa94ef8bD5ffEE41947b4585a84BdA5a3d3DA6E",
            privateKey = "0x829e924fdf021ba3dbbc4225edfece9aca04b929d6e75613329ca6f1d31c0bb4",
            name = "U8"
        ),
        GanacheUser(
            address = "0x1dF62f291b2E969fB0849d99D9Ce41e2F137006e",
            privateKey = "0xb0057716d5917badaf911b193b12b910811c1497b5bada8d7711f758981c3773",
            name = "U9"
        )
    )

    val SYSTEM_OWNER = DEFAULT_ACCOUNTS[0]
    val INIT_USER = DEFAULT_ACCOUNTS[1]

    /**
     * Available project creators (not system owner)
     */
    val PROJECT_CREATORS = DEFAULT_ACCOUNTS.drop(1).take(2)

    /**
     * Available funders
     */
    val FUNDERS = DEFAULT_ACCOUNTS.drop(3)
}

