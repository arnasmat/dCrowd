package com.arnasmat.dcrowd.data.repository

import com.arnasmat.dcrowd.data.web3.CrowdSourcingWrapper
import com.arnasmat.dcrowd.data.web3.Milestone
import com.arnasmat.dcrowd.data.web3.Project
import com.arnasmat.dcrowd.data.web3.UserCredentials
import com.arnasmat.dcrowd.data.web3.UserManager
import com.arnasmat.dcrowd.data.web3.Web3Config
import com.arnasmat.dcrowd.data.web3.Web3ConfigManager
import com.arnasmat.dcrowd.data.web3.Web3Result
import kotlinx.coroutines.flow.Flow
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrowdFundingRepository @Inject constructor(
    private val crowdSourcingWrapper: CrowdSourcingWrapper,
    private val userManager: UserManager,
    private val web3ConfigManager: Web3ConfigManager
) {

    val currentUserFlow: Flow<UserCredentials?> = userManager.currentUserFlow
    val web3ConfigFlow: Flow<Web3Config?> = web3ConfigManager.configFlow

    suspend fun initializeConnection(rpcUrl: String, contractAddress: String): Web3Result<Unit> {
        return crowdSourcingWrapper.initializeConnection(rpcUrl, contractAddress)
    }

    suspend fun loginUser(address: String, privateKey: String, name: String = ""): Web3Result<Unit> {
        return try {
            userManager.loginUser(address, privateKey, name)
            Web3Result.Success(Unit)
        } catch (e: Exception) {
            Web3Result.Error(e.message ?: "Failed to login", e)
        }
    }

    suspend fun getCurrentUser(): UserCredentials? {
        return userManager.getCurrentUser()
    }

    suspend fun logoutUser() {
        userManager.clearCurrentUser()
    }

    suspend fun getWeb3Config(): Web3Config? {
        return web3ConfigManager.getConfig()
    }

    suspend fun saveWeb3Config(config: Web3Config) {
        web3ConfigManager.saveConfig(config)
    }

    suspend fun createProject(
        name: String,
        headerImageUrl: String,
        description: String,
        milestones: List<Milestone>
    ): Web3Result<TransactionReceipt> {
        if (milestones.isEmpty()) {
            return Web3Result.Error("Project must have at least one milestone")
        }

        if (name.isBlank()) {
            return Web3Result.Error("Project name cannot be empty")
        }

        return crowdSourcingWrapper.createProject(
            name,
            headerImageUrl,
            description,
            milestones
        )
    }

    suspend fun fundProjectInEth(
        projectIdx: BigInteger,
        amountInEth: BigDecimal
    ): Web3Result<TransactionReceipt> {
        if (amountInEth <= BigDecimal.ZERO) {
            return Web3Result.Error("Funding amount must be greater than 0")
        }

        val amountInWei = ethToWei(amountInEth)
        return crowdSourcingWrapper.fundProject(projectIdx, amountInWei)
    }

    suspend fun fundProject(
        projectIdx: BigInteger,
        amountInWei: BigInteger
    ): Web3Result<TransactionReceipt> {
        if (amountInWei <= BigInteger.ZERO) {
            return Web3Result.Error("Funding amount must be greater than 0")
        }

        return crowdSourcingWrapper.fundProject(projectIdx, amountInWei)
    }

    suspend fun checkCurrentMilestone(projectIdx: BigInteger): Web3Result<TransactionReceipt> {
        return crowdSourcingWrapper.checkCurrentMilestone(projectIdx)
    }

    suspend fun checkMilestone(projectIdx: BigInteger): Web3Result<TransactionReceipt> {
        return crowdSourcingWrapper.checkMilestone(projectIdx)
    }

    suspend fun stopProject(projectIdx: BigInteger): Web3Result<TransactionReceipt> {
        return crowdSourcingWrapper.stopProject(projectIdx)
    }

    suspend fun getProject(projectIdx: BigInteger): Web3Result<Project> {
        return crowdSourcingWrapper.getProject(projectIdx)
    }

    suspend fun getSysOwner(): Web3Result<String> {
        return crowdSourcingWrapper.getSysOwner()
    }

    suspend fun getBalanceInEth(address: String): Web3Result<BigDecimal> {
        return when (val result = crowdSourcingWrapper.getBalance(address)) {
            is Web3Result.Success -> {
                val balanceInEth = Convert.fromWei(
                    BigDecimal(result.data),
                    Convert.Unit.ETHER
                )
                Web3Result.Success(balanceInEth)
            }
            is Web3Result.Error -> Web3Result.Error(result.message, result.throwable)
            Web3Result.Loading -> Web3Result.Loading
        }
    }

    suspend fun getCurrentUserBalance(): Web3Result<BigDecimal> {
        val user = getCurrentUser() ?: return Web3Result.Error("No user selected")
        return getBalanceInEth(user.address)
    }

    suspend fun getCurrentBlockNumber(): Web3Result<BigInteger> {
        return crowdSourcingWrapper.getCurrentBlockNumber()
    }

    fun weiToEth(wei: BigInteger): BigDecimal {
        return Convert.fromWei(BigDecimal(wei), Convert.Unit.ETHER)
    }

    fun ethToWei(eth: BigDecimal): BigInteger {
        return Convert.toWei(eth, Convert.Unit.ETHER).toBigInteger()
    }
}

