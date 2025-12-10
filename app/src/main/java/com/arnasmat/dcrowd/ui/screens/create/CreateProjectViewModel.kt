package com.arnasmat.dcrowd.ui.screens.create

import android.util.Log
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject
import kotlin.collections.get
import kotlin.text.set

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val repository: CrowdFundingRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CreateProjectState())
    val state: StateFlow<CreateProjectState> = _state.asStateFlow()

    fun createProject() {
        if (state.value.name .isBlank()) {
            _state.newUiState(CreateProjectUiState.Error("Project name is required"))
            return
        }

        if (state.value.description.isBlank()) {
            _state.newUiState(CreateProjectUiState.Error("Description is required"))
            return
        }

        if (state.value.milestones.isEmpty()) {
            _state.newUiState(CreateProjectUiState.Error("At least one milestone is required"))
            return
        }


        val convertedMilestones = try {
            state.value.milestones.map { input ->
                Milestone(
                    goalAmount = repository.ethToWei(BigDecimal(input.goalAmountEth)),
                    deadline = BigInteger.valueOf(input.deadlineTimestamp)
                )
            }
        } catch (e: Exception) {
            _state.newUiState(CreateProjectUiState.Error("Invalid milestone data: ${e.message}"))
            return
        }

        viewModelScope.launch {

            _state.newUiState(CreateProjectUiState.Loading)

            when (val result = repository.createProject(
                name = state.value.name,
                headerImageUrl = state.value.imageUrl,
                description = state.value.description,
                milestones = convertedMilestones
            )) {
                is Web3Result.Success -> {
                    val events = CrowdSourcing.getProjectCreatedEvents(result.data)
                    val projectIdx = events.firstOrNull()?.projectIdx?.subtract(BigInteger.ONE)
                    // TODO: remove this when backend returns correct idx, not len

                    Log.i("TEMPTEMP", projectIdx.toString())
                    _state.update { it.copy(
                        uiState = CreateProjectUiState.Success("Project created successfully!"),
                        createdProjectIdx = projectIdx
                    ) }
                }

                is Web3Result.Error -> {
                    _state.newUiState(CreateProjectUiState.Error(result.message))
                }

                Web3Result.Loading -> {}
            }
        }
    }

    fun updateName(newName: String) {
        _state.update { it.copy(name = newName) }
    }

    fun updateImageUrl(newImageUrl: String) {
        _state.update { it.copy(imageUrl = newImageUrl) }
    }

    fun updateDescription(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    fun addNewMilestone() {
        _state.update { currentState ->
            val updatedMilestones = currentState.milestones + MilestoneInput(
                goalAmountEth = "10",
                deadlineTimestamp = System.currentTimeMillis() / 1000 + 86400
            )
            currentState.copy(milestones = updatedMilestones)
        }
    }

    fun updateMilestoneGoal(index: Int, newGoal: String) {
        if (index < 0 && index >= _state.value.milestones.size) return

        _state.update { currentState ->
            val updatedMilestones = currentState.milestones.toMutableList().apply {
                this[index] = this[index].copy(goalAmountEth = newGoal)
            } as List<MilestoneInput>
            currentState.copy(milestones = updatedMilestones)
        }
    }

    fun updateMilestoneDeadline(index: Int, newDeadlineTimestamp: Long) {
        if (index < 0 && index >= _state.value.milestones.size) return

        _state.update { currentState ->
            val updatedMilestones = currentState.milestones.toMutableList().apply {
                this[index] = this[index].copy(deadlineTimestamp = newDeadlineTimestamp)
            } as List<MilestoneInput>
            currentState.copy(milestones = updatedMilestones)
        }
    }

    fun removeMilestone(index: Int) {
        if (index < 0 && index >= _state.value.milestones.size) return

        _state.update { currentState ->
            val updatedMilestones = currentState.milestones.toMutableList().apply {
                this.removeAt(index)
            } as List<MilestoneInput>
            currentState.copy(milestones = updatedMilestones)
        }
    }


    private fun MutableStateFlow<CreateProjectState>.newUiState(newState: CreateProjectUiState) {
        this.update { it.copy(uiState = newState) }
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

data class CreateProjectState(
    val uiState: CreateProjectUiState = CreateProjectUiState.Editing,
    val createdProjectIdx: BigInteger? = null,
    val name: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val milestones: List<MilestoneInput> = listOf(
        MilestoneInput(
            "10",
            System.currentTimeMillis() / 1000 + 86400
        )
    )
)

