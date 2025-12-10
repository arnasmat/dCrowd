package com.arnasmat.dcrowd.ui.screens.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
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
import org.web3j.utils.Convert

enum class FundingUnit {
    ETH, GWEI, WEI
}

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {

    private val _project = MutableStateFlow<Project?>(null)
    val project: StateFlow<Project?> = _project.asStateFlow()

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadProject(projectIdx: BigInteger) {
        Log.i("TEMPTEMP", projectIdx.toString())
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

    fun fundProject(projectIdx: BigInteger, amountString: String, unit: FundingUnit = FundingUnit.ETH) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.FundingInProgress

            val amount = try {
                BigDecimal(amountString)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Invalid amount")
                return@launch
            }

            if (amount <= BigDecimal.ZERO) {
                _uiState.value = DetailUiState.Error("Amount must be greater than 0")
                return@launch
            }

            // Convert to Wei based on selected unit
            val amountInWei = when (unit) {
                FundingUnit.ETH -> Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
                FundingUnit.GWEI -> Convert.toWei(amount, Convert.Unit.GWEI).toBigInteger()
                FundingUnit.WEI -> amount.toBigInteger()
            }

            when (val result = repository.fundProject(projectIdx, amountInWei)) {
                is Web3Result.Success -> {
                    _uiState.value = DetailUiState.FundingSuccess(
                        "Successfully funded with $amountString ${unit.name}!"
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

