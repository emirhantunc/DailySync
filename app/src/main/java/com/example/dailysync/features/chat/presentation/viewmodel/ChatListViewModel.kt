package com.example.dailysync.features.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.features.chat.domain.model.ChatRoom
import com.example.dailysync.features.chat.domain.model.FollowingUser
import com.example.dailysync.features.chat.domain.usecases.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatListUiState(
    val chatRooms: List<ChatRoom> = emptyList(),
    val followingUsers: List<FollowingUser> = emptyList(),
    val isLoading: Boolean = false,
    val navigateToChatRoom: Pair<String, String>? = null
)

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    fun fetchChatRooms() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            chatUseCases.getChatRoomsUseCase().collect { result ->
                result.onSuccess { chatRooms ->
                    _uiState.update { it.copy(chatRooms = chatRooms, isLoading = false) }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun fetchFollowingUsers() {
        viewModelScope.launch {
            chatUseCases.getFollowingUsersUseCase().onSuccess { users ->
                _uiState.update { it.copy(followingUsers = users) }
            }
        }
    }

    fun startNewChat(userId: String, userName: String) {
        viewModelScope.launch {
            chatUseCases.createOrGetChatRoomUseCase(userId).onSuccess { chatRoomId ->
                _uiState.update { it.copy(navigateToChatRoom = chatRoomId to userName) }
            }
        }
    }

    fun resetNavigation() {
        _uiState.update { it.copy(navigateToChatRoom = null) }
    }
}
