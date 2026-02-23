package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.features.chat.domain.repository.ChatRepository
import com.example.dailysync.features.chat.domain.repository.FollowingUser
import javax.inject.Inject

class GetFollowingUsersUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(): Result<List<FollowingUser>> {
        return repository.getFollowingUsers()
    }
}
