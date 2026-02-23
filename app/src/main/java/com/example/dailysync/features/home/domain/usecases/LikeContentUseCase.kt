package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.core.enums.FeedType
import com.example.dailysync.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class LikeContentUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(contentId: String, contentType: FeedType): Result<Unit> {
        return repository.likeContent(contentId, contentType)
    }
}
