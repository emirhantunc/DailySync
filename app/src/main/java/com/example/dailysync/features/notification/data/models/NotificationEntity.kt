package com.example.dailysync.features.notification.data.models

data class NotificationEntity(
    val id: String = "",
    val userId: String = "",
    val type: String = "",
    val actorId: String = "",
    val actorName: String = "",
    val targetId: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false
)
