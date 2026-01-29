package com.example.dailysync.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.features.auth.domain.exceptions.SingInError
import com.example.dailysync.features.auth.domain.usecases.AuthUseCases
import com.example.dailysync.features.auth.presentation.mapper.toSignModel
import com.example.dailysync.features.auth.presentation.models.SignUpPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignUpPresentationState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val error: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpPresentationState())
    val uiState: StateFlow<SignUpPresentationState> = _uiState.asStateFlow()

    fun signUp() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true, error = null)
            }

            val result = authUseCases.singUpUseCase(
                model = SignUpPresentation(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                ).toSignModel()
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    val errorRes = when (error) {
                        is SingInError.NetworkError -> R.string.network_error
                        else -> R.string.network_error
                    }
                    _uiState.update { it.copy(isLoading = false, error = errorRes) }
                }
            )
        }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, error = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, error = null) }
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue, error = null) }
    }
}