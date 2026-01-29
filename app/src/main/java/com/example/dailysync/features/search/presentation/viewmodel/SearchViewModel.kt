package com.example.dailysync.features.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.features.search.domain.exception.SearchError
import com.example.dailysync.features.search.domain.usecases.SearchUseCase
import com.example.dailysync.features.search.presentation.mapper.toSearchPresentationList
import com.example.dailysync.features.search.presentation.model.SearchPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SearchState(
    val users: List<SearchPresentation> = emptyList(),
    val error: Int? = null
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
                        is SearchError.NetworkError->{
                            _uiState.update {state->
                                state.copy(
                                    error = R.string.network_error
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}