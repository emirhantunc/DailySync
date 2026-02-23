package com.example.dailysync.features.notification.domain.usecases

import com.example.dailysync.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> {
        return repository.markAsRead(notificationId)
    }
}
