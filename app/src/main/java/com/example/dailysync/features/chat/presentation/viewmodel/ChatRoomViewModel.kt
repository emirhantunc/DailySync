package com.example.dailysync.features.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.features.chat.domain.model.Message
import com.example.dailysync.features.chat.domain.usecases.GetMessagesUseCase
import com.example.dailysync.features.chat.domain.usecases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatRoomUiState(
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false,
    val messageText: String = ""
)

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatRoomUiState())
    val uiState: StateFlow<ChatRoomUiState> = _uiState.asStateFlow()

    private var currentChatRoomId: String? = null

    fun loadMessages(chatRoomId: String) {
        currentChatRoomId = chatRoomId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getMessagesUseCase(chatRoomId).collect { result ->
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
            sendMessageUseCase(chatRoomId, text).onSuccess {
                _uiState.update { it.copy(messageText = "") }
            }
        }
    }
}
