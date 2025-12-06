package com.arnasmat.dcrowd.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.web3.GanacheUser
import com.arnasmat.dcrowd.data.web3.Project
import com.arnasmat.dcrowd.data.web3.Web3Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val repository: CrowdFundingRepository,
    private val ganacheConfig: com.arnasmat.dcrowd.data.web3.GanacheConfig
) : ViewModel() {

    val currentUser: StateFlow<GanacheUser?> = repository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _projects = MutableStateFlow<List<ProjectWithIndex>>(emptyList())
    val projects: StateFlow<List<ProjectWithIndex>> = _projects.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        // Automatically connect to the configured contract address on startup
        connectToContract(ganacheConfig.contractAddress)
    }

    fun connectToContract(contractAddress: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = repository.initializeAddress(contractAddress)) {
                is Web3Result.Success -> {
                    _isConnected.value = true
                    _uiState.value = UiState.Success("Connected successfully")
                    loadProjects()
                }
                is Web3Result.Error -> {
                    _uiState.value = UiState.Error(result.message)
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            // Load projects 0-9 (adjust based on your needs)
            val projectsList = mutableListOf<ProjectWithIndex>()

            for (i in 0..9) {
                when (val result = repository.getProject(BigInteger.valueOf(i.toLong()))) {
                    is Web3Result.Success -> {
                        projectsList.add(ProjectWithIndex(BigInteger.valueOf(i.toLong()), result.data))
                    }
                    is Web3Result.Error -> {
                        // Project doesn't exist or error, stop loading
                        break
                    }
                    Web3Result.Loading -> {}
                }
            }

            _projects.value = projectsList
            _uiState.value = if (projectsList.isEmpty()) {
                UiState.Success("No projects found")
            } else {
                UiState.Success("Loaded ${projectsList.size} projects")
            }
        }
    }

    fun refreshBalance() {
        viewModelScope.launch {
            repository.getCurrentUserBalance()
        }
    }
}

data class ProjectWithIndex(
    val index: BigInteger,
    val project: Project
)

sealed class UiState {
    data object Initial : UiState()
    data object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

