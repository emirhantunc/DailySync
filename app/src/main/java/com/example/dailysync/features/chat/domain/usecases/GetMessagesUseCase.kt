package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.features.chat.domain.model.Message
import com.example.dailysync.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(chatRoomId: String): Flow<Result<List<Message>>> {
        return repository.getMessages(chatRoomId)
    }
}
