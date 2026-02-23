package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.profile.domain.models.post.ProfilePostModel
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserPostsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<List<ProfilePostModel>> {
        return repository.getUserPosts(userId)
    }
}
