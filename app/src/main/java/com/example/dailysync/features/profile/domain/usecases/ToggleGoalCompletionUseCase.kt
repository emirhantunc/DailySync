package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ToggleGoalCompletionUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(postId: String, goalId: String): Result<Unit> {
        return repository.toggleGoalCompletion(postId, goalId)
    }
}
