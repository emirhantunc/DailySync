package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject


class ToggleFollowUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return repository.toggleFollow(userId)
    }
}