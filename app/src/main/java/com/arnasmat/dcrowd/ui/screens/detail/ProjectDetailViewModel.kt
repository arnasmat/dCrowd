package com.arnasmat.dcrowd.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.sol.CrowdSourcing
import com.arnasmat.dcrowd.data.web3.Project
import com.arnasmat.dcrowd.data.web3.Web3Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadProject(projectIdx: BigInteger) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            when (val result = repository.getProject(projectIdx)) {
                is Web3Result.Success -> {
                    _project.value = result.data
                    _uiState.value = DetailUiState.Success
                }
                is Web3Result.Error -> {
                    _uiState.value = DetailUiState.Error(result.message)
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun fundProject(projectIdx: BigInteger, amountEth: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.FundingInProgress

            val amount = try {
                BigDecimal(amountEth)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Invalid amount")
                return@launch
            }

            if (amount <= BigDecimal.ZERO) {
                _uiState.value = DetailUiState.Error("Amount must be greater than 0")
                return@launch
            }

            when (val result = repository.fundProjectInEth(projectIdx, amount)) {
                is Web3Result.Success -> {
                    // Get funding details from event
                    val events = CrowdSourcing.getProjectFundedEvents(result.data)
                    val event = events.firstOrNull()

                    _uiState.value = DetailUiState.FundingSuccess(
                        "Successfully funded with $amountEth ETH!"
                    )

                    // Reload project to show updated funding
                    loadProject(projectIdx)
                }
                is Web3Result.Error -> {
                    _uiState.value = DetailUiState.Error("Funding failed: ${result.message}")
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun stopProject(projectIdx: BigInteger) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            when (val result = repository.stopProject(projectIdx)) {
                is Web3Result.Success -> {
                    _uiState.value = DetailUiState.Success
                    loadProject(projectIdx)
                }
                is Web3Result.Error -> {
                    _uiState.value = DetailUiState.Error("Failed to stop project: ${result.message}")
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun resetFundingState() {
        _uiState.value = DetailUiState.Success
    }
}

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data object Success : DetailUiState()
    data object FundingInProgress : DetailUiState()
    data class FundingSuccess(val message: String) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

