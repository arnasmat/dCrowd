package com.arnasmat.dcrowd.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.web3.ProjectWithIndex
import com.arnasmat.dcrowd.data.web3.Web3Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectListState())
    val state = _state.asStateFlow()

    init {
        observeConfiguration()
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            repository.web3ConfigFlow.collect { config ->
                if (config != null) {
                    _state.update { it.copy(isConnected = true) }
                    loadProjects()
                } else {
                    _state.update { it.copy(isConnected = false) }
                }
            }
        }
    }

    fun connectToContract(rpcUrl: String, contractAddress: String) {
        viewModelScope.launch {
            _state.newUiState(UiState.Loading)

            when (val result = repository.initializeConnection(rpcUrl, contractAddress)) {
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

            when (val result = repository.getAllActiveProjects()) {
                is Web3Result.Success -> {
                    _state.update { it.copy(
                        projects = result.data,
                        uiState = if (result.data.isEmpty()) {
                            UiState.Success("No projects found")
                        } else {
                            UiState.Success("Loaded ${result.data.size} projects")
                        }
                    ) }
                }
                is Web3Result.Error -> {
                    _state.newUiState(UiState.Error(result.message))
                }
                Web3Result.Loading -> {
                    // Should not happen but handle gracefully
                    _state.newUiState(UiState.Loading)
                }
            }
        }
    }
}

private fun MutableStateFlow<ProjectListState>.newUiState(newState: UiState) {
    this.update { it.copy(uiState = newState) }
}


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

