package com.example.dailysync.features.uploadposts.domain.repository

import com.example.dailysync.features.uploadposts.domain.models.UserGoal

interface UploadPostsRepository {
    suspend fun uploadPosts(selectedGoals: List<UserGoal>): Result<Unit>
}
