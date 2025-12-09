package com.arnasmat.dcrowd.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.web3.GanacheConfig
import com.arnasmat.dcrowd.data.web3.GanacheUser
import com.arnasmat.dcrowd.data.web3.Web3Result
import com.arnasmat.dcrowd.ui.screens.projects.ProjectListState
import com.arnasmat.dcrowd.ui.screens.projects.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CrowdFundingRepository,
    private val ganacheConfig: GanacheConfig
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState(
        contractAddress = ganacheConfig.contractAddress,
        currentUser = repository.currentUserFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            ).value,
    ))

    val state = _state.asStateFlow()

    init {
        initializeDefaultUser()
        // Initialize with the configured contract address
        initializeWithAddress(ganacheConfig.contractAddress)
    }

    private fun initializeDefaultUser() {
        viewModelScope.launch {
            repository.initializeDefaultUser()
            refreshUserBalance()
        }
    }

    fun initializeWithAddress(address: String) {
        viewModelScope.launch {
            _state.newUiState(SettingsUiState.Loading)

            when (val result = repository.initializeAddress(address)) {
                is Web3Result.Success -> {
                    _state.update {
                        it.copy(
                            contractAddress = address,
                            uiState = SettingsUiState.Success("Connected to contract")
                        )
                    }
                }
                is Web3Result.Error -> {
                    _state.newUiState(SettingsUiState.Error(result.message))
                }
                else -> {}
            }
        }
    }

    fun switchUser(user: GanacheUser) {
        viewModelScope.launch {
            repository.switchUser(user)
            refreshUserBalance()
            _state.newUiState(SettingsUiState.Success("Switched to ${user.name}"))
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

    fun getAvailableUsers(excludeSystemOwner: Boolean = false): List<GanacheUser> {
        return repository.getAvailableUsers(excludeSystemOwner)
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
    val contractAddress: String? = null,
    val currentUser: GanacheUser? = null,
    val userBalance: BigDecimal? = null,
    val uiState: SettingsUiState = SettingsUiState.Initial
)

