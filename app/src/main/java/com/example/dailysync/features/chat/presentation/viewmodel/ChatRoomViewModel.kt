package com.example.dailysync.features.chat.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.features.chat.domain.model.Message
import com.example.dailysync.features.chat.domain.usecases.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatRoomUiState(
    val currentUserId: String? = null,
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val messageText: String = ""
)

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private var currentChatRoomId: String? = null

    init {
        getCurrentUserId()
    }

    fun getCurrentUserId(){
        viewModelScope.launch {
            chatUseCases.getCurrentUserIdUseCase().collect{result ->
                result.fold(
                    onSuccess = { userId ->
                        _uiState.update { it.copy(currentUserId = userId) }

                    },
                    onFailure = {
                        Log.d("ChatRoomViewModel", "getCurrentUserId: ${it.message}")

                    }
                )
            }
        }
    }
    fun loadMessages(chatRoomId: String) {
        currentChatRoomId = chatRoomId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            chatUseCases.getMessagesUseCase(chatRoomId).collect { result ->
                result.onSuccess { messages ->
                    _uiState.update { it.copy(messages = messages, isLoading = false) }
                }.onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun updateMessageText(text: String) {
        _uiState.update { it.copy(messageText = text) }
    }

    fun sendMessage() {
        val chatRoomId = currentChatRoomId ?: return
        val text = _uiState.value.messageText.trim()

        if (text.isEmpty()) return

        viewModelScope.launch {
            chatUseCases.sendMessageUseCase(chatRoomId, text).onSuccess {
                _uiState.update { it.copy(messageText = "") }
            }
        }
    }
}
