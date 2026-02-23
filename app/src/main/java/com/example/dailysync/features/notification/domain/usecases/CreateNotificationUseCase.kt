package com.example.dailysync.features.notification.domain.usecases

import com.example.dailysync.features.notification.domain.models.NotificationDomain
import com.example.dailysync.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class CreateNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationDomain): Result<Unit> {
        return repository.createNotification(notification)
    }
}
