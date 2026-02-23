package com.example.dailysync.features.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.R
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.auth.domain.usecases.AuthUseCases
import com.example.dailysync.features.chat.domain.usecases.CreateOrGetChatRoomUseCase
import com.example.dailysync.features.home.presentation.model.ThoughtPresentation
import com.example.dailysync.features.profile.domain.usecases.ProfileUseCases
import com.example.dailysync.features.profile.presentation.mapper.toProfilePostPresentationList
import com.example.dailysync.features.profile.presentation.mapper.toProfilePresentation
import com.example.dailysync.features.profile.presentation.models.ProfilePresentation
import com.example.dailysync.features.profile.presentation.models.post.ProfilePostPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProfileState(
    val ownId: String? = null,
    val profile: ProfilePresentation = ProfilePresentation(),
    val userPosts: List<ProfilePostPresentation> = emptyList(),
    val userThoughts: List<ThoughtPresentation> = emptyList(),
    val error: ErrorType? = null,
    val loading: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val logoutSuccess: Boolean = false,
    val navigateToChatRoom: Pair<String, String>? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val createOrGetChatRoomUseCase: CreateOrGetChatRoomUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState())

    val uiState: StateFlow<ProfileState> = _uiState.asStateFlow()


    fun getOwnId() {
        viewModelScope.launch {
            profileUseCases.getOwnIdUseCase()
                .onStart {
                    _uiState.update { it.copy(loading = true) }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { id ->
                            _uiState.update { it.copy(ownId = id) }
                        },
                        onFailure = {

                        }
                    )

                }
        }
    }

    fun getProfile(id: String? = null) {
        viewModelScope.launch {
            profileUseCases.getProfileUseCase(id)
                .onStart {
                    _uiState.update { it.copy(loading = true) }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { profile ->
                            val profilePresentation = profile.toProfilePresentation()
                            _uiState.update { state ->
                                state.copy(
                                    profile = profilePresentation,
                                    loading = false,
                                    error = null
                                )
                            }
                            getUserActivities(profilePresentation.id)
                        },
                        onFailure = { error ->
                            when (error) {
                                is AppExceptions.Network.NoInternet -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            error = ErrorType.NETWORK_ERROR,
                                            loading = false
                                        )
                                    }
                                }

                                is AppExceptions.Profile.ProfileNotFound -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            error = ErrorType.ACCOUNT_NOT_FOUND,
                                            loading = false
                                        )
                                    }
                                }

                                else -> {
                                    _uiState.update { state ->
                                        state.copy(
                                            error = ErrorType.UNKNOWN_ERROR,
                                            loading = false
                                        )
                                    }
                                }

                            }
                        }
                    )
                }
        }
    }

    fun toggleFollow(userId: String) {
        viewModelScope.launch {
            profileUseCases.toggleFollowUseCase(userId)
        }
    }

    fun showLogoutDialog() {
        _uiState.update { state ->
            state.copy(showLogoutDialog = true)
        }
    }

    fun hideLogoutDialog() {
        _uiState.update { state ->
            state.copy(showLogoutDialog = false)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            hideLogoutDialog()
            val result = profileUseCases.signOutUseCase()
            result.fold(
                onSuccess = {
                    _uiState.update { state ->
                        state.copy(logoutSuccess = true)
                    }
                },
                onFailure = {
                    _uiState.update { state ->
                        state.copy(error = ErrorType.UNKNOWN_ERROR)
                    }
                }
            )
        }
    }

    fun getUserActivities(userId: String) {
        viewModelScope.launch {
            val postsResult = profileUseCases.getUserPostsUseCase(userId)

            postsResult.fold(
                onSuccess = { posts ->
                    _uiState.update { it.copy(userPosts = posts.toProfilePostPresentationList()) }
                },
                onFailure = {
                    _uiState.update { it.copy(error = ErrorType.UNKNOWN_ERROR) }

                }
            )
        }
    }

    fun toggleGoalCompletion(postId: String, goalId: String) {
        val currentPosts = _uiState.value.userPosts.toMutableList()
        val postIndex = currentPosts.indexOfFirst { it.postId == postId }

        if (postIndex != -1) {
            val post = currentPosts[postIndex]
            val updatedGoals = post.goals.goals.map { goal ->
                if (goal.id == goalId) {
                    goal.copy(isCompleted = !goal.isCompleted)
                } else {
                    goal
                }
            }

            val updatedPost = post.copy(
                goals = post.goals.copy(goals = updatedGoals)
            )
            currentPosts[postIndex] = updatedPost
            _uiState.update { it.copy(userPosts = currentPosts) }
        }

        viewModelScope.launch {
            profileUseCases.toggleGoalCompletionUseCase(postId, goalId)
        }
    }

    fun createOrGetChatRoom(userId: String, userName: String) {
        viewModelScope.launch {
            val result = createOrGetChatRoomUseCase(userId)
            result.onSuccess { chatRoomId ->
                _uiState.update { it.copy(navigateToChatRoom = chatRoomId to userName) }
            }
        }
    }

    fun resetChatNavigation() {
        _uiState.update { it.copy(navigateToChatRoom = null) }
    }
}

