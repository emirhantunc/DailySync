package com.example.dailysync.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.features.home.presentation.viewmodel.UiState
import com.example.dailysync.features.profile.domain.exceptions.GetProfileError
import com.example.dailysync.features.profile.domain.usecases.GetProfileUseCase
import com.example.dailysync.features.profile.presentation.mapper.toProfilePresentation
import com.example.dailysync.features.profile.presentation.models.ProfilePresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProfileState(
    val profile: ProfilePresentation = ProfilePresentation(),
    val error: Int? = null

)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())

    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()


    fun getProfile(id: String? = null) {
        viewModelScope.launch {
            val result = getProfileUseCase(id)
            result.fold(
                onSuccess = { profile ->
                    _uiState.update { state ->
                        state.copy(
                            profile = profile.toProfilePresentation()
                        )
                    }

                },
                onFailure = { error ->
                    when (error) {
                        is GetProfileError.NetworkError -> {
                            _uiState.update { state ->
                                state.copy(
                                    error = R.string.network_error
                                )
                            }
                        }

                        is GetProfileError.ProfileNotFound -> {

                        }

                        is GetProfileError.UnknownError -> {

                        }
                    }

                }
            )
        }
    }
}