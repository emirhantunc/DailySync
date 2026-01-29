package com.example.dailysync.features.home.domain.repository

import com.example.dailysync.features.home.domain.model.PostModel


interface HomeRepository {
    suspend fun fetchGoals(): Result<List<PostModel>>
    suspend fun shareGoal(post: PostModel): Result<Unit>
}