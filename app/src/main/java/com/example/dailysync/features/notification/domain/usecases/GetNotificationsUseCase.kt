package com.example.dailysync.features.notification.domain.usecases

import com.example.dailysync.features.notification.domain.models.NotificationDomain
import com.example.dailysync.features.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<Result<List<NotificationDomain>>> {
        return repository.getNotifications()
    }
}
