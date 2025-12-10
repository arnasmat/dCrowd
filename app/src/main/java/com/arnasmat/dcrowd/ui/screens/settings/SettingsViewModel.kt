package com.arnasmat.dcrowd.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.web3.UserCredentials
import com.arnasmat.dcrowd.data.web3.Web3Config
import com.arnasmat.dcrowd.data.web3.Web3Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        observeConfiguration()
        observeCurrentUser()
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            repository.web3ConfigFlow.collect { config ->
                _state.update {
                    it.copy(
                        rpcUrl = config?.rpcUrl ?: "",
                        contractAddress = config?.contractAddress ?: ""
                    )
                }
            }
        }
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            repository.currentUserFlow.collect { user ->
                _state.update { it.copy(currentUser = user) }
                if (user != null) {
                    refreshUserBalance()
                }
            }
        }
    }

    fun updateRpcUrl(url: String) {
        _state.update { it.copy(rpcUrl = url) }
    }

    fun updateContractAddress(address: String) {
        _state.update { it.copy(contractAddress = address) }
    }

    fun updateUserAddress(address: String) {
        _state.update { it.copy(userAddress = address) }
    }

    fun updateUserPrivateKey(privateKey: String) {
        _state.update { it.copy(userPrivateKey = privateKey) }
    }

    fun updateUserName(name: String) {
        _state.update { it.copy(userName = name) }
    }

    fun saveConfiguration() {
        viewModelScope.launch {
            _state.newUiState(SettingsUiState.Loading)

            val rpcUrl = _state.value.rpcUrl.trim()
            val contractAddress = _state.value.contractAddress.trim()

            if (rpcUrl.isEmpty()) {
                _state.newUiState(SettingsUiState.Error("RPC URL is required"))
                return@launch
            }

            if (contractAddress.isEmpty()) {
                _state.newUiState(SettingsUiState.Error("Contract address is required"))
                return@launch
            }

            when (val result = repository.initializeConnection(rpcUrl, contractAddress)) {
                is Web3Result.Success -> {
                    _state.newUiState(SettingsUiState.Success("Configuration saved successfully"))
                }
                is Web3Result.Error -> {
                    _state.newUiState(SettingsUiState.Error(result.message))
                }
                else -> {}
            }
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            _state.newUiState(SettingsUiState.Loading)

            val address = _state.value.userAddress.trim()
            val privateKey = _state.value.userPrivateKey.trim()
            val name = _state.value.userName.trim()

            if (address.isEmpty()) {
                _state.newUiState(SettingsUiState.Error("Address is required"))
                return@launch
            }

            if (privateKey.isEmpty()) {
                _state.newUiState(SettingsUiState.Error("Private key is required"))
                return@launch
            }

            when (val result = repository.loginUser(address, privateKey, name)) {
                is Web3Result.Success -> {
                    _state.update {
                        it.copy(
                            userAddress = "",
                            userPrivateKey = "",
                            userName = ""
                        )
                    }
                    _state.newUiState(SettingsUiState.Success("Logged in successfully"))
                }
                is Web3Result.Error -> {
                    _state.newUiState(SettingsUiState.Error(result.message))
                }
                else -> {}
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.logoutUser()
            _state.update { it.copy(userBalance = null) }
            _state.newUiState(SettingsUiState.Success("Logged out successfully"))
        }
    }

    fun refreshUserBalance() {
        viewModelScope.launch {
            when (val result = repository.getCurrentUserBalance()) {
                is Web3Result.Success -> {
                    _state.update { it.copy(userBalance = result.data) }
                }
                is Web3Result.Error -> {
                    _state.update { it.copy(userBalance = null) }
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun clearMessage() {
        _state.newUiState(SettingsUiState.Initial)
    }
}

private fun MutableStateFlow<SettingsState>.newUiState(newState: SettingsUiState) {
    this.update { it.copy(uiState = newState) }
}

sealed class SettingsUiState {
    data object Initial : SettingsUiState()
    data object Loading : SettingsUiState()
    data class Success(val message: String) : SettingsUiState()
    data class Error(val message: String) : SettingsUiState()
}

data class SettingsState(
    val rpcUrl: String = "",
    val contractAddress: String = "",
    val userAddress: String = "",
    val userPrivateKey: String = "",
    val userName: String = "",
    val currentUser: UserCredentials? = null,
    val userBalance: BigDecimal? = null,
    val uiState: SettingsUiState = SettingsUiState.Initial
)

