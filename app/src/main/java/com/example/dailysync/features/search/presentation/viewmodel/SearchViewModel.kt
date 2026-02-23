package com.example.dailysync.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.search.domain.usecases.SearchUseCase
import com.example.dailysync.features.search.presentation.mapper.toSearchPresentationList
import com.example.dailysync.features.search.presentation.models.SearchPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchState(
    val users: List<SearchPresentation> = emptyList(),
    val errorType: ErrorType? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchState())

    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()


    fun searchUser(name: String) {
        viewModelScope.launch {
            val result = searchUseCase(name = name)

            result.fold(
                onSuccess = { users ->
                    _uiState.update { state ->
                        state.copy(
                            users = users.toSearchPresentationList()
                        )
                    }
                },
                onFailure = { error ->
                    when (error) {
                        is AppExceptions.Network.NoInternet->{
                            _uiState.update {state->
                                state.copy(
                                    errorType = ErrorType.NETWORK_ERROR
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}