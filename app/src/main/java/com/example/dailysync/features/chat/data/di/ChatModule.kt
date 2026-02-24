package com.example.dailysync.features.chat.data.di

import com.example.dailysync.features.chat.domain.repository.ChatRepository
import com.example.dailysync.features.chat.domain.usecases.ChatUseCases
import com.example.dailysync.features.chat.domain.usecases.CreateOrGetChatRoomUseCase
import com.example.dailysync.features.chat.domain.usecases.GetChatRoomsUseCase
import com.example.dailysync.features.chat.domain.usecases.GetCurrentUserIdUseCase
import com.example.dailysync.features.chat.domain.usecases.GetFollowingUsersUseCase
import com.example.dailysync.features.chat.domain.usecases.GetMessagesUseCase
import com.example.dailysync.features.chat.domain.usecases.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ChatModule {

    @Provides
    @ViewModelScoped
    fun provideChatUseCases(
        createOrGetChatRoomUseCase: CreateOrGetChatRoomUseCase,
        getChatRoomsUseCase: GetChatRoomsUseCase,
        getFollowingUsersUseCase: GetFollowingUsersUseCase,
        getMessagesUseCase: GetMessagesUseCase,
        sendMessageUseCase: SendMessageUseCase,
        getCurrentUserIdUseCase: GetCurrentUserIdUseCase
    ): ChatUseCases {
        return ChatUseCases(
            createOrGetChatRoomUseCase = createOrGetChatRoomUseCase,
            getChatRoomsUseCase = getChatRoomsUseCase,
            getFollowingUsersUseCase = getFollowingUsersUseCase,
            getMessagesUseCase = getMessagesUseCase,
            sendMessageUseCase = sendMessageUseCase,
            getCurrentUserIdUseCase = getCurrentUserIdUseCase
        )
    }
}