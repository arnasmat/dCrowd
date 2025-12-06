package com.arnasmat.dcrowd.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arnasmat.dcrowd.data.repository.CrowdFundingRepository
import com.arnasmat.dcrowd.data.sol.CrowdSourcing
import com.arnasmat.dcrowd.data.web3.Milestone
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
class CreateProjectViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateProjectUiState>(CreateProjectUiState.Editing)
    val uiState: StateFlow<CreateProjectUiState> = _uiState.asStateFlow()

    private val _createdProjectIdx = MutableStateFlow<BigInteger?>(null)
    val createdProjectIdx: StateFlow<BigInteger?> = _createdProjectIdx.asStateFlow()

    fun createProject(
        name: String,
        imageUrl: String,
        description: String,
        milestones: List<MilestoneInput>
    ) {
        viewModelScope.launch {
            // Validation
            if (name.isBlank()) {
                _uiState.value = CreateProjectUiState.Error("Project name is required")
                return@launch
            }

            if (description.isBlank()) {
                _uiState.value = CreateProjectUiState.Error("Description is required")
                return@launch
            }

            if (milestones.isEmpty()) {
                _uiState.value = CreateProjectUiState.Error("At least one milestone is required")
                return@launch
            }

            // Check if system owner
            if (repository.isCurrentUserSystemOwner()) {
                _uiState.value = CreateProjectUiState.Error("System owner cannot create projects! Switch to a different user.")
                return@launch
            }

            _uiState.value = CreateProjectUiState.Loading

            // Convert milestones
            val convertedMilestones = try {
                milestones.map { input ->
                    Milestone(
                        goalAmount = repository.ethToWei(BigDecimal(input.goalAmountEth)),
                        deadline = BigInteger.valueOf(input.deadlineTimestamp)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = CreateProjectUiState.Error("Invalid milestone data: ${e.message}")
                return@launch
            }

            when (val result = repository.createProject(
                name = name,
                headerImageUrl = imageUrl,
                description = description,
                milestones = convertedMilestones
            )) {
                is Web3Result.Success -> {
                    // Extract project index from event
                    val events = CrowdSourcing.getProjectCreatedEvents(result.data)
                    val projectIdx = events.firstOrNull()?.projectIdx

                    _createdProjectIdx.value = projectIdx
                    _uiState.value = CreateProjectUiState.Success("Project created successfully! Index: $projectIdx")
                }
                is Web3Result.Error -> {
                    _uiState.value = CreateProjectUiState.Error(result.message)
                }
                Web3Result.Loading -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = CreateProjectUiState.Editing
        _createdProjectIdx.value = null
    }
}

data class MilestoneInput(
    val goalAmountEth: String,
    val deadlineTimestamp: Long
)

sealed class CreateProjectUiState {
    data object Editing : CreateProjectUiState()
    data object Loading : CreateProjectUiState()
    data class Success(val message: String) : CreateProjectUiState()
    data class Error(val message: String) : CreateProjectUiState()
}

