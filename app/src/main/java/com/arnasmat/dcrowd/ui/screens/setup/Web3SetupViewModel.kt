package com.arnasmat.dcrowd.ui.screens.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.web3.GanacheUser
import com.arnasmat.dcrowd.data.web3.Web3Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

/**
 * ViewModel for Web3 setup and configuration
 */
@HiltViewModel
class Web3SetupViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {

    // Current user flow from repository
    val currentUser: StateFlow<GanacheUser?> = repository.currentUserFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    // UI State
    private val _uiState = MutableStateFlow<SetupUiState>(SetupUiState.Initial)
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    // Contract address
    private val _contractAddress = MutableStateFlow<String?>(null)
    val contractAddress: StateFlow<String?> = _contractAddress.asStateFlow()

    // User balance
    private val _userBalance = MutableStateFlow<BigDecimal?>(null)
    val userBalance: StateFlow<BigDecimal?> = _userBalance.asStateFlow()

    init {
        initializeDefaultUser()
    }

    /**
     * Initialize with default user
     */
    private fun initializeDefaultUser() {
        viewModelScope.launch {
            repository.initializeDefaultUser()
            refreshUserBalance()
        }
    }


    /**
     * Initialize with existing contract address
     */
    fun initializeWithAddress(address: String) {
        viewModelScope.launch {
            _uiState.value = SetupUiState.Loading("Connecting to contract...")

            when (val result = repository.initializeAddress(address)) {
                is Web3Result.Success -> {
                    _contractAddress.value = address
                    _uiState.value = SetupUiState.Success("Connected to contract")

                    // Verify by getting system owner
                    verifySysOwner()
                }
                is Web3Result.Error -> {
                    _uiState.value = SetupUiState.Error(result.message)
                }
                Web3Result.Loading -> {
                    _uiState.value = SetupUiState.Loading("Connecting...")
                }
            }
        }
    }

    /**
     * Verify contract by getting system owner
     */
    private fun verifySysOwner() {
        viewModelScope.launch {
            when (val result = repository.getSysOwner()) {
                is Web3Result.Success -> {
                    val sysOwnerInfo = repository.getSystemOwnerInfo()
                    val matches = result.data.equals(sysOwnerInfo.address, ignoreCase = true)

                    if (matches) {
                        _uiState.value = SetupUiState.Success(
                            "✓ Connected! System Owner: ${result.data.take(10)}..."
                        )
                    } else {
                        _uiState.value = SetupUiState.Error(
                            "System owner mismatch. Expected: ${sysOwnerInfo.address}"
                        )
                    }
                }
                is Web3Result.Error -> {
                    _uiState.value = SetupUiState.Error("Failed to verify: ${result.message}")
                }
                Web3Result.Loading -> {}
            }
        }
    }

    /**
     * Switch user account
     */
    fun switchUser(user: GanacheUser) {
        viewModelScope.launch {
            repository.switchUser(user)
            refreshUserBalance()
            _uiState.value = SetupUiState.Success("Switched to ${user.name}")
        }
    }

    /**
     * Refresh current user's balance
     */
    fun refreshUserBalance() {
        viewModelScope.launch {
            when (val result = repository.getCurrentUserBalance()) {
                is Web3Result.Success -> {
                    _userBalance.value = result.data
                }
                is Web3Result.Error -> {
                    _userBalance.value = null
                }
                Web3Result.Loading -> {}
            }
        }
    }

    /**
     * Get available users (exclude system owner for project creation)
     */
    fun getAvailableUsers(excludeSystemOwner: Boolean = false): List<GanacheUser> {
        return repository.getAvailableUsers(excludeSystemOwner)
    }

    /**
     * Check if current user is system owner
     */
    suspend fun isCurrentUserSystemOwner(): Boolean {
        return repository.isCurrentUserSystemOwner()
    }

    /**
     * Clear UI state
     */
    fun clearUiState() {
        _uiState.value = SetupUiState.Initial
    }
}

/**
 * UI state for setup screen
 */
sealed class SetupUiState {
    data object Initial : SetupUiState()
    data class Loading(val message: String) : SetupUiState()
    data class Success(val message: String) : SetupUiState()
    data class Error(val message: String) : SetupUiState()
}

