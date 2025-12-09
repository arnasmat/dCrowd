package com.arnasmat.dcrowd.data.web3

const val BASE_URL = "http://10.0.2.2:8545" // Android emu -> localhost
const val CONTRACT_ADDRESS = "0xB22A5FbDFD3260865BF1F6EC636F54bE4B6B2548"

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
            address = "0x3c70032309dfa3Fe3Def41D655EF493f3d8aE6f0",
            privateKey = "0x11d30810fd899cd7504afed1cba382a70cc8cc007636ae0cf423b21c74a66fb8",
            name = "sysOwner"
        ),
        GanacheUser(
            address = "0xE29c3729Fdd6CB37C7FCBd0Bdc98336eeeAa5b4C",
            privateKey = "0xfd2c4b826189f91257a0be4c08611955d9bb12d243e9a341d9edf05d9db28c36",
            name = "U1"
        ),
        GanacheUser(
            address = "0xe718e9c66b8bd5DAba5e4Cc2eb8e03C51e676E05",
            privateKey = "0xd34e51f336c0fc5fd6d5b1ac3a431a5447bceb68e0d66333cd96294cb62f5662",
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

