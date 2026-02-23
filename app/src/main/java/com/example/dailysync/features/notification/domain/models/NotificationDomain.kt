package com.example.dailysync.features.notification.domain.models

data class NotificationDomain(
    val id: String,
    val userId: String,
    val type: String,
    val actorId: String,
    val actorName: String,
    val targetId: String,
    val timestamp: Long,
    val isRead: Boolean
)
