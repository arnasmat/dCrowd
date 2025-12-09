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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.UIntArraySerializer
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val repository: CrowdFundingRepository,
    private val ganacheConfig: com.arnasmat.dcrowd.data.web3.GanacheConfig
) : ViewModel() {

    val currentUser: StateFlow<GanacheUser?> = repository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)


//    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
//    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _state = MutableStateFlow(ProjectListState())
    val state = _state.asStateFlow()

    init {
        connectToContract(ganacheConfig.contractAddress)
    }

    fun connectToContract(contractAddress: String) {
        viewModelScope.launch {
            _state.newUiState(UiState.Loading)

            when (val result = repository.initializeAddress(contractAddress)) {
                is Web3Result.Success -> {
                    _state.update { it.copy(
                        isConnected = true,
                        uiState = UiState.Success("Connected successfully"),
                    ) }
                    loadProjects()
                }
                is Web3Result.Error -> {
                    _state.newUiState(UiState.Error(result.message))
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun loadProjects() {
        viewModelScope.launch {
            _state.newUiState(UiState.Loading)

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

            _state.update { it.copy(
                projects = projectsList,
                uiState = if (projectsList.isEmpty()) {
                    UiState.Success("No projects found")
                } else {
                    UiState.Success("Loaded ${projectsList.size} projects")
                }
                ) }

        }
    }

    fun refreshBalance() {
        viewModelScope.launch {
            repository.getCurrentUserBalance()
        }
    }
}

private fun MutableStateFlow<ProjectListState>.newUiState(newState: UiState) {
    this.update { it.copy(uiState = newState) }
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

data class ProjectListState(
    val isConnected: Boolean = false,
    val projects: List<ProjectWithIndex> = emptyList(),
    val uiState: UiState = UiState.Initial
)

