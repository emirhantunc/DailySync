package com.example.dailysync.features.home.domain.repository

import com.example.dailysync.core.enums.FeedType
import com.example.dailysync.features.home.domain.models.FeedModel
import com.example.dailysync.features.home.domain.models.PostModel
import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.home.domain.models.UserProfile
import kotlinx.coroutines.flow.Flow


interface HomeRepository {

    suspend fun shareThought(thought: ThoughtModel): Result<Unit>
    suspend fun getLikedFeeds(): Result<List<String>>
    fun fetchFeeds(): Flow<Result<List<FeedModel>>>
    suspend fun likeContent(contentId: String, contentType: FeedType): Result<Unit>

    fun getUnreadNotificationCount(): Flow<Result<Int>>

    suspend fun unlikeContent(
        contentId: String, contentType: FeedType
    ): Result<Unit>
}


