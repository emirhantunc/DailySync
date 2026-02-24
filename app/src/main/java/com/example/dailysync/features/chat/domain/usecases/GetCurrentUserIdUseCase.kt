package com.example.dailysync.features.chat.domain.usecases

import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    operator fun invoke(): Flow<Result<String?>> {
        return userProfileRepository.getCurrentUserId()
    }
}