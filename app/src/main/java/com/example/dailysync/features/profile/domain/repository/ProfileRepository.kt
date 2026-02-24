package com.example.dailysync.features.profile.domain.repository

import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.profile.domain.models.ProfileModel
import com.example.dailysync.features.profile.domain.models.post.ProfilePostModel
import kotlinx.coroutines.flow.Flow


interface ProfileRepository {
    suspend fun toggleFollow(id: String): Result<Unit>
    suspend fun getUserPosts(userId: String): Result<List<ProfilePostModel>>
    suspend fun getUserThoughts(userId: String): Result<List<ThoughtModel>>
    suspend fun toggleGoalCompletion(postId: String, goalId: String): Result<Unit>
}