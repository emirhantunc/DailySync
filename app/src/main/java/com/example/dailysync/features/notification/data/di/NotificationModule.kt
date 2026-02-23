package com.example.dailysync.features.notification.data.di

import com.example.dailysync.features.notification.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object NotificationModule {

    @Provides
    @ViewModelScoped
    fun provideNotificationUseCases(
        getNotificationsUseCase: GetNotificationsUseCase,
        createNotificationUseCase: CreateNotificationUseCase,
        markAsReadUseCase: MarkAsReadUseCase,
        getUnreadCountUseCase: GetUnreadCountUseCase
    ): NotificationUseCases {
        return NotificationUseCases(
            getNotificationsUseCase = getNotificationsUseCase,
            createNotificationUseCase = createNotificationUseCase,
            markAsReadUseCase = markAsReadUseCase,
            getUnreadCountUseCase = getUnreadCountUseCase
        )
    }
}
