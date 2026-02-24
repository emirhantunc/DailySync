package com.example.dailysync.features.chat.domain.usecases

data class ChatUseCases(
    val getChatRoomsUseCase: GetChatRoomsUseCase,
    val getMessagesUseCase: GetMessagesUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val createOrGetChatRoomUseCase: CreateOrGetChatRoomUseCase,
    val getFollowingUsersUseCase: GetFollowingUsersUseCase,
    val getCurrentUserIdUseCase: GetCurrentUserIdUseCase

)