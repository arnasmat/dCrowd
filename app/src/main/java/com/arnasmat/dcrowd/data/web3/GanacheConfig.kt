package com.arnasmat.dcrowd.data.web3

const val BASE_URL = "http://10.0.2.2:8545" // Android emu -> localhost
data class GanacheConfig(
    val rpcUrl: String = BASE_URL, // Android emulator to localhost
    val networkId: String = "1337", // Ganache default network ID
    val chainId: Long = 1337L
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
            address = "0x5f97B587F34217DDfE854a37f6C43930cF40b16F",
            privateKey = "0xd0181b987c9f994cbd8c7ad65c01f15f131bcdf5d8f3c0f874d757c8a1764aba",
            name = "sysOwner"
        ),
        GanacheUser(
            address = "0x21B2CB19029cC2c3333Adda07485eb7637b75BeC",
            privateKey = "0x6136062b2ad9a270f32fd0e8f25e34b60a16b19aefdb9340c05f1665ddced2fe",
            name = "U1"
        ),
        GanacheUser(
            address = "0x32DeeD540F218E77835D82ab54620a73382b11AA",
            privateKey = "0xb6a402bc4fd15cb2ddc5942732c95691fcdca8e581a5ec30ae9b3dadba24d4ee",
            name = "U2"
        ),
        GanacheUser(
            address = "0x2F990dF8A9e7c64926A95B2732c8de8e5Fa3226d",
            privateKey = "0xe611ad3b8212d04ca128e7a6a0c59a761f5bfca3de683cf279f51358db57f8b6",
            name = "U3"
        ),
        GanacheUser(
            address = "0x29a1b10E45687cd08d8348FC10dB29fa304c8E04",
            privateKey = "0x76d58fbc457d362052fc76a5a2d3d8e00af5f9fbc207cd6dda57f68f87d8663a",
            name = "U4"
        ),
        GanacheUser(
            address = "0x7737a8B2Cd581Aabe5015fab574AefF72f43Ffcf",
            privateKey = "0xe7712bf048254f793c52a38a99c66ddf814aa10bcb625c47596c0af3be939f25",
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

