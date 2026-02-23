package com.example.dailysync.features.notification.presentation.models

data class NotificationPresentation(
    val id: String = "",
    val userId: String = "",
    val type: String = "",
    val actorId: String = "",
    val actorName: String = "",
    val targetId: String = "",
    val timestamp: Long = 0L,
    val isRead: Boolean = false
)
