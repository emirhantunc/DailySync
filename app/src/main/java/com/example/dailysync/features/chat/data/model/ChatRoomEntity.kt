package com.example.dailysync.features.chat.data.model

data class ChatRoomEntity(
    val id: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = 0L
)
