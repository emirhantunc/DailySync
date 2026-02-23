package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetLikedFeedsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        return repository.getLikedFeeds()
    }
}

