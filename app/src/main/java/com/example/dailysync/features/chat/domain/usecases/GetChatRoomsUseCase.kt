package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.features.chat.domain.model.ChatRoom
import com.example.dailysync.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatRoomsUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<Result<List<ChatRoom>>> {
        return repository.getChatRooms()
    }
}
