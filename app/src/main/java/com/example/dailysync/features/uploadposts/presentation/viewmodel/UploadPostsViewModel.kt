package com.example.dailysync.features.uploadposts.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.uploadposts.domain.usecases.UploadPostsUseCases
import com.example.dailysync.features.uploadposts.presentation.mapper.toUserGoalList
import com.example.dailysync.features.uploadposts.presentation.mapper.toUserGoalPresentationList
import com.example.dailysync.features.uploadposts.presentation.model.UserGoalPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadPostsUiState(
    val userGoals: List<UserGoalPresentation> = emptyList(),
    val errorType: ErrorType? = null,
    val isLoading: Boolean = false,
    val uploadSuccess: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class UploadPostsViewModel @Inject constructor(
    private val uploadPostsUseCases: UploadPostsUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadPostsUiState())
    val uiState: StateFlow<UploadPostsUiState> = _uiState.asStateFlow()


    fun toggleGoalSelection(goalId: String) {
        _uiState.update { currentState ->
            val updatedGoals = currentState.userGoals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(isSelected = !goal.isSelected)
                } else {
                    goal
                }
            }
            currentState.copy(userGoals = updatedGoals)
        }
    }

    fun uploadSelectedGoals() {
        viewModelScope.launch {
            val selectedGoals = _uiState.value.userGoals.filter { it.isSelected }

            if (selectedGoals.isEmpty()) {
                _uiState.update { it.copy(errorType = ErrorType.UNKNOWN_ERROR) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            val result = uploadPostsUseCases.uploadPostsUseCase(selectedGoals.toUserGoalList())
            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            uploadSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    when (error) {
                        is AppExceptions.Network.NoInternet -> {
                            _uiState.update { state ->
                                state.copy(
                                    errorType = ErrorType.NETWORK_ERROR,
                                    isLoading = false
                                )
                            }
                        }
                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    errorType = ErrorType.UNKNOWN_ERROR,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    fun resetErrorType() {
        _uiState.update { it.copy(errorType = null) }
    }

    fun resetUploadSuccess() {
        _uiState.update { it.copy(uploadSuccess = false) }
    }

    fun addNewGoal(goal: String, timeRange: String, target: String) {
        val newGoal = UserGoalPresentation(
            goal = goal,
            id = System.currentTimeMillis().toString(),
            timeRange = timeRange,
            target = target,
            isSelected = false
        )

        _uiState.update { currentState ->
            val updatedGoals = currentState.userGoals + newGoal
            currentState.copy(
                userGoals = updatedGoals,
                saveSuccess = true
            )
        }
    }

    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
