package com.example.dailysync.features.chat.domain.repository

import com.example.dailysync.features.chat.domain.model.ChatRoom
import com.example.dailysync.features.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

data class FollowingUser(
    val id: String = "",
    val name: String = ""
)

interface ChatRepository {
    fun getChatRooms(): Flow<Result<List<ChatRoom>>>
    fun getMessages(chatRoomId: String): Flow<Result<List<Message>>>
    suspend fun sendMessage(chatRoomId: String, text: String): Result<Unit>
    suspend fun createOrGetChatRoom(otherUserId: String): Result<String>
    suspend fun getFollowingUsers(): Result<List<FollowingUser>>
}
