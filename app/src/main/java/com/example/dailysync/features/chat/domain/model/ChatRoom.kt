package com.example.dailysync.features.chat.domain.model

data class ChatRoom(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L,
    val otherUserName: String = "",
    val otherUserId: String = ""
)
