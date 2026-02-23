package com.example.dailysync.features.notification.domain.usecases

import com.example.dailysync.features.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnreadCountUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Flow<Result<Int>> {
        return repository.getUnreadCount()
    }
}
