package com.example.dailysync.features.notification.domain.usecases

data class NotificationUseCases(
    val getNotificationsUseCase: GetNotificationsUseCase,
    val createNotificationUseCase: CreateNotificationUseCase,
    val markAsReadUseCase: MarkAsReadUseCase,
    val getUnreadCountUseCase: GetUnreadCountUseCase
)
