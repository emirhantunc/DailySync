package com.example.dailysync.features.uploadposts.domain.usecases

import com.example.dailysync.features.uploadposts.domain.models.UserGoal
import com.example.dailysync.features.uploadposts.domain.repository.UploadPostsRepository
import javax.inject.Inject

class UploadPostsUseCase @Inject constructor(
    private val repository: UploadPostsRepository
) {
    suspend operator fun invoke(selectedGoals: List<UserGoal>): Result<Unit> {
        return repository.uploadPosts(selectedGoals)
    }
}
