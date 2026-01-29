package com.example.dailysync.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.features.auth.domain.exceptions.SingInError
import com.example.dailysync.features.auth.domain.usecases.AuthUseCases
import com.example.dailysync.features.auth.presentation.mapper.toSignModel
import com.example.dailysync.features.auth.presentation.models.SignInPresentation
import com.example.dailysync.features.search.presentation.viewmodel.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SignInPresentationState(
    val email: String = "",
    val password: String = "",
    val error: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInPresentationState())

    val uiState: StateFlow<SignInPresentationState> = _uiState.asStateFlow()

    fun signIn() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true)
            }
            val result = authUseCases.singInUseCase(
                model = SignInPresentation(
                    _uiState.value.email,
                    _uiState.value.password
                ).toSignModel()
            )

            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(isLoading = false, isSuccess = true)
                    }

                }, onFailure = { error ->
                    when (error) {
                        is SingInError.NetworkError -> {
                            _uiState.update { state ->
                                state.copy(isLoading = false, error = R.string.network_error)
                            }
                        }
                    }

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

}