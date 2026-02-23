package com.example.dailysync.features.chat.data.model

data class MessageEntity(
    val id: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
