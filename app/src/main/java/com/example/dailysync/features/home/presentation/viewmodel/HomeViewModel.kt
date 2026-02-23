package com.example.dailysync.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.enums.FeedType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.home.domain.usecases.HomeUseCases
import com.example.dailysync.features.home.presentation.mapper.toFeedPresentationList
import com.example.dailysync.features.home.presentation.model.FeedPresentation
import com.example.dailysync.features.home.presentation.model.PostPresentation
import com.example.dailysync.features.home.presentation.model.ThoughtPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UiState(
    val posts: List<PostPresentation> = emptyList(),
    val thoughts: List<ThoughtPresentation> = emptyList(),
    val feeds: List<FeedPresentation> = emptyList(),
    val likedFeeds: List<String> = emptyList(),
    val errorType: ErrorType? = null,
    val networkConnection: Boolean = true,
    val userName: String = "",
    val shareSuccess: Boolean = false,
    val isSharing: Boolean = false,
    val unreadNotificationCount: Int = 0,
    val userId: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        getUserProfile()
        getLikedItems()
        getNotificationNumber()
    }

    fun getLikedItems() {
        viewModelScope.launch {
            val feedsResult = homeUseCases.getLikedFeedsUseCase()

            feedsResult.fold(
                onSuccess = { likedFeeds ->
                    _uiState.update { it.copy(likedFeeds = likedFeeds) }
                },
                onFailure = { }
            )
        }
    }

    fun getNotificationNumber() {
        viewModelScope.launch {
            val result = homeUseCases.getUnreadNotificationCountUseCase()
            result.collect { result ->
                result.fold(
                    onSuccess = { count ->
                        _uiState.update { it.copy(unreadNotificationCount = count) }

                    },
                    onFailure = {

                    }
                )
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            val result = homeUseCases.getUserProfileUseCase()
            result.collect {result ->
                result.fold(
                    onSuccess = { userProfile ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                userName = userProfile.name,
                                userId = userProfile.id
                            )
                        }
                    },
                    onFailure = { error ->
                        when (error) {
                            is AppExceptions.Network.NoInternet -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        errorType = ErrorType.NETWORK_ERROR
                                    )
                                }
                            }

                            is AppExceptions.Auth.UserNotLoggedIn -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        errorType = ErrorType.AUTH_ERROR
                                    )
                                }
                            }

                            else -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        errorType = ErrorType.UNKNOWN_ERROR
                                    )
                                }
                            }
                        }
                    }
                )

            }

        }
    }

    fun resetErrorType() {
        _uiState.update { it.copy(errorType = null) }
    }

    fun resetShareSuccess() {
        _uiState.update { it.copy(shareSuccess = false) }
    }

    fun shareThought(thoughtText: String) {
        if (thoughtText.isBlank()) {
            _uiState.update { it.copy(errorType = ErrorType.EMPTY_THOUGHT) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSharing = true) }

            val userId = _uiState.value.userId
            val userName = _uiState.value.userName
            val currentTime = System.currentTimeMillis()

            val thoughtModel = com.example.dailysync.features.home.domain.models.ThoughtModel(
                thought = thoughtText,
                name = userName,
                userId = userId,
                releaseTime = currentTime
            )

            val result = homeUseCases.shareThoughtUseCase(thoughtModel)

            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(
                            shareSuccess = true,
                            isSharing = false
                        )
                    }
                    fetchFeeds()
                },
                onFailure = { error ->
                    when (error) {
                        is AppExceptions.Network.NoInternet -> {
                            _uiState.update { state ->
                                state.copy(
                                    errorType = ErrorType.NETWORK_ERROR,
                                    isSharing = false
                                )
                            }
                        }

                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    errorType = ErrorType.UNKNOWN_ERROR,
                                    isSharing = false
                                )
                            }
                        }
                    }
                }
            )
        }
    }


    fun fetchFeeds() {
        viewModelScope.launch {
            homeUseCases.fetchFeedsUseCase().collect { result ->
                result.fold(
                    onSuccess = { feeds ->
                        _uiState.update { currentState ->
                            currentState.copy(feeds = feeds.toFeedPresentationList())
                        }
                    },
                    onFailure = { error ->
                        when (error) {
                            is AppExceptions.Network.NoInternet -> {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        errorType = ErrorType.NETWORK_ERROR,
                                        networkConnection = false
                                    )
                                }
                            }

                            else -> {
                                _uiState.update { currentState ->
                                    currentState.copy(errorType = ErrorType.UNKNOWN_ERROR)
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    fun toggleLike(contentId: String, contentType: FeedType) {
        val isCurrentlyLiked = _uiState.value.likedFeeds.contains(contentId)
        updateLikeState(contentId, isLiked = !isCurrentlyLiked)
        viewModelScope.launch {
            val result = if (isCurrentlyLiked) {
                homeUseCases.unlikeContentUseCase(contentId, contentType)
            } else {
                homeUseCases.likeContentUseCase(contentId, contentType)
            }

            result.onFailure { error ->
                updateLikeState(contentId, isLiked = isCurrentlyLiked)
                handleError(error)
            }

        }
    }

    private fun updateLikeState(contentId: String, isLiked: Boolean) {
        _uiState.update { state ->
            val newLikedFeeds = if (isLiked) {
                state.likedFeeds + contentId
            } else {
                state.likedFeeds - contentId
            }
            state.copy(likedFeeds = newLikedFeeds)
        }
    }

    private fun handleError(error: Throwable) {
        val errorType = when (error) {
            is AppExceptions.Network.NoInternet -> ErrorType.NETWORK_ERROR
            else -> ErrorType.UNKNOWN_ERROR
        }
        _uiState.update { it.copy(errorType = errorType) }
    }

}
