package com.arnasmat.dcrowd.data.web3

import com.arnasmat.dcrowd.data.sol.CrowdSourcing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.TransactionReceipt
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

sealed class Web3Result<out T> {
    data class Success<T>(val data: T) : Web3Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : Web3Result<Nothing>()
    data object Loading : Web3Result<Nothing>()
}

@Singleton
class CrowdSourcingWrapper @Inject constructor(
    private val web3Factory: Web3Factory,
    private val userManager: UserManager,
    private val web3ConfigManager: Web3ConfigManager
) {

    private var web3j: Web3j? = null

    suspend fun initializeConnection(rpcUrl: String, contractAddress: String) = withContext(Dispatchers.IO) {
        try {
            web3j = web3Factory.createWeb3j(rpcUrl)

            val version = web3j?.web3ClientVersion()?.send()
            if (version?.hasError() == true) {
                throw Exception("Failed to connect to network: ${version.error.message}")
            }

            val config = Web3Config(
                rpcUrl = rpcUrl,
                contractAddress = contractAddress
            )
            web3ConfigManager.saveConfig(config)

            Web3Result.Success(Unit)
        } catch (e: Exception) {
            Web3Result.Error("Failed to initialize Web3: ${e.message}", e)
        }
    }

    private suspend fun getContract(): CrowdSourcing {
        val currentUser = userManager.getCurrentUser()
            ?: throw IllegalStateException("No user logged in. Please login first.")

        val config = web3ConfigManager.getConfig()
            ?: throw IllegalStateException("Web3 not configured. Please configure in settings.")

        if (web3j == null) {
            web3j = web3Factory.createWeb3j(config.rpcUrl)
        }

        val credentials = web3Factory.createCredentials(currentUser)
        val gasProvider = web3Factory.getGasProvider()

        return CrowdSourcing.load(
            config.contractAddress,
            web3j ?: throw IllegalStateException("Web3j not initialized"),
            credentials,
            gasProvider
        )
    }


    suspend fun getSysOwner(): Web3Result<String> = withContext(Dispatchers.IO) {
        try {
            val contract = getContract()
            val owner = contract.sysOwner().send()
            Web3Result.Success(owner)
        } catch (e: Exception) {
            Web3Result.Error("Failed to get system owner: ${e.message}", e)
        }
    }

    suspend fun createProject(
        name: String,
        headerImageUrl: String,
        description: String,
        milestones: List<Milestone>
    ): Web3Result<TransactionReceipt> = withContext(Dispatchers.IO) {
        try {

            val contract = getContract()
            val milestoneInfoList = milestones.map {
                CrowdSourcing.MilestoneInfo(it.goalAmount, it.deadline)
            }

            val receipt = contract.createProject(
                name,
                headerImageUrl,
                description,
                milestoneInfoList
            ).send()

            Web3Result.Success(receipt)
        } catch (e: Exception) {
            Web3Result.Error("Failed to create project: ${e.message}", e)
        }
    }

    suspend fun fundProject(
        projectIdx: BigInteger,
        amountInWei: BigInteger
    ): Web3Result<TransactionReceipt> = withContext(Dispatchers.IO) {
        try {
            val contract = getContract()
            val receipt = contract.fundProject(projectIdx, amountInWei).send()
            Web3Result.Success(receipt)
        } catch (e: Exception) {
            Web3Result.Error("Failed to fund project: ${e.message}", e)
        }
    }

    suspend fun checkCurrentMilestone(projectIdx: BigInteger): Web3Result<TransactionReceipt> =
        withContext(Dispatchers.IO) {
            try {
                val contract = getContract()
                val receipt = contract.checkCurrentMilestone(projectIdx).send()
                Web3Result.Success(receipt)
            } catch (e: Exception) {
                Web3Result.Error("Failed to check current milestone: ${e.message}", e)
            }
        }

    suspend fun checkMilestone(projectIdx: BigInteger): Web3Result<TransactionReceipt> =
        withContext(Dispatchers.IO) {
            try {
                val contract = getContract()
                val receipt = contract.checkMilestone(projectIdx).send()
                Web3Result.Success(receipt)
            } catch (e: Exception) {
                Web3Result.Error("Failed to check milestone: ${e.message}", e)
            }
        }

    suspend fun stopProject(projectIdx: BigInteger): Web3Result<TransactionReceipt> =
        withContext(Dispatchers.IO) {
            try {
                val contract = getContract()
                val receipt = contract.stopProject(projectIdx).send()
                Web3Result.Success(receipt)
            } catch (e: Exception) {
                Web3Result.Error("Failed to stop project: ${e.message}", e)
            }
        }

    suspend fun getProject(projectIdx: BigInteger): Web3Result<Project> =
        withContext(Dispatchers.IO) {
            try {
                val contract = getContract()
                val result = contract.projects(projectIdx).send()

                val project = Project(
                    owner = result.component1(),
                    name = result.component2(),
                    headerImageUrl = result.component3(),
                    description = result.component4(),
                    totalFunded = result.component5(),
                    currentMilestoneIdx = result.component6(),
                    isActive = result.component7()
                )

                Web3Result.Success(project)
            } catch (e: Exception) {
                Web3Result.Error("Failed to get project: ${e.message}", e)
            }
        }

    suspend fun getBalance(address: String): Web3Result<BigInteger> = withContext(Dispatchers.IO) {
        try {
            val balance = web3j?.ethGetBalance(address, DefaultBlockParameterName.LATEST)?.send()
            if (balance?.hasError() == true) {
                throw Exception(balance.error.message)
            }
            Web3Result.Success(balance?.balance ?: BigInteger.ZERO)
        } catch (e: Exception) {
            Web3Result.Error("Failed to get balance: ${e.message}", e)
        }
    }

    suspend fun getCurrentBlockNumber(): Web3Result<BigInteger> = withContext(Dispatchers.IO) {
        try {
            val blockNumber = web3j?.ethBlockNumber()?.send()
            if (blockNumber?.hasError() == true) {
                throw Exception(blockNumber.error.message)
            }
            Web3Result.Success(blockNumber?.blockNumber ?: BigInteger.ZERO)
        } catch (e: Exception) {
            Web3Result.Error("Failed to get block number: ${e.message}", e)
        }
    }

    suspend fun observeProjectCreatedEvents(
        startBlock: BigInteger,
        endBlock: BigInteger
    ) = withContext(Dispatchers.IO) {
        try {
            val contract = getContract()
            contract.projectCreatedEventFlowable(
                org.web3j.protocol.core.DefaultBlockParameter.valueOf(startBlock),
                org.web3j.protocol.core.DefaultBlockParameter.valueOf(endBlock)
            )
        } catch (e: Exception) {
            throw e
        }
    }
}

data class Milestone(
    val goalAmount: BigInteger,
    val deadline: BigInteger
)

data class Project(
    val owner: String,
    val name: String,
    val headerImageUrl: String,
    val description: String,
    val totalFunded: BigInteger,
    val currentMilestoneIdx: BigInteger,
    val isActive: Boolean
)

