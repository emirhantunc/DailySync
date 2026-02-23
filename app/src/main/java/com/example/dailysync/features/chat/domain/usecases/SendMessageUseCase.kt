package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(chatRoomId: String, text: String): Result<Unit> {
        return repository.sendMessage(chatRoomId, text)
    }
}
