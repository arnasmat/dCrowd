package com.arnasmat.dcrowd.data.web3

const val BASE_URL = "http://10.0.2.2:8545" // Android emu -> localhost
const val CONTRACT_ADDRESS = "0x488AbEF9C20E874C9c6dB01f336E5503B81a7E7D"

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
            address = "0xa12A900c8ceD65FEd7b8162819098c15c7287FCc",
            privateKey = "0x47706c09ee4b047508a8d42c0b66badc49036bbd444cfc95a7f5a5f0fdc5647a",
            name = "sysOwner"
        ),
        GanacheUser(
            address = "0xb31CF9647f3BedFDe0055b567e42D6a9e11B6eDE",
            privateKey = "0xba76712515bcb5a8038d814f87d53d873577b3f0c8d230b86e0b004cb86bbe8d",
            name = "U1"
        ),
        GanacheUser(
            address = "0x6403ec956DFA24ff4347C6178551e775adFeB1CA",
            privateKey = "0x2f25619f824433886bea991ee79ac78797311bb5853773e6c537be28e8398384",
            name = "U2"
        ),
        GanacheUser(
            address = "0x7Be8b48f49019980DC6a4fbf16d0339EC66654e9",
            privateKey = "0x4e69ed6eb6b0d53161f7a8a267a57fc11f0d3667f3ecc0e9ea3c42d501fa2868",
            name = "U3"
        ),
        GanacheUser(
            address = "0x57AA9845B94dF9194D6bfa203C64c54d7596E704",
            privateKey = "0x9a306386340e2280b9fdea5a5529033e7654986d50ccb6dbb5edd48248591681",
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

