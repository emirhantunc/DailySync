package com.example.dailysync.features.notification.domain.repository

import com.example.dailysync.features.notification.domain.models.NotificationDomain
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<Result<List<NotificationDomain>>>
    suspend fun createNotification(notification: NotificationDomain): Result<Unit>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun getUnreadCount(): Flow<Result<Int>>
}
