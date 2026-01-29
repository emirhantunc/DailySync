package com.example.dailysync.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.features.home.domain.exceptions.FetchError
import com.example.dailysync.features.home.domain.exceptions.ShareError
import com.example.dailysync.features.home.domain.usecases.HomeUseCases
import com.example.dailysync.features.home.presentation.mapper.toPostModel
import com.example.dailysync.features.home.presentation.mapper.toPostPresentationList
import com.example.dailysync.features.home.presentation.model.PostPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiState(
    val post: List<PostPresentation> = emptyList(),
    val errorMessage: Int? = null,
    val networkConnection: Boolean = true
)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchGoals()
    }

    fun fetchGoals() {
        viewModelScope.launch {
            val result = homeUseCases.fetchUseCase()
            result.fold(
                onSuccess = { posts ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            post = posts.toPostPresentationList()
                        )
                    }
                },
                onFailure = { error ->
                    when (error) {
                        is FetchError.NetworkError -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    errorMessage = R.string.network_error,
                                    networkConnection = false
                                )
                            }
                        }
                    }
                }
            )
        }
    }


    fun sharePost(post: PostPresentation) {
        viewModelScope.launch {
            val result = homeUseCases.shareGoalUseCase(post.toPostModel())
            result.fold(
                onSuccess = {

                },
                onFailure = {exception ->
                    when (exception) {
                        is ShareError.NetworkError -> {
                            _uiState.update { state ->
                                state.copy(errorMessage = R.string.network_error)
                            }
                        }
                    }
                }
            )
        }
    }
}
