package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class CreateOrGetChatRoomUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(otherUserId: String): Result<String> {
        return repository.createOrGetChatRoom(otherUserId)
    }
}
