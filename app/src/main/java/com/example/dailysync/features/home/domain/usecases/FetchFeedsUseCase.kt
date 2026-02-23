package com.example.dailysync.features.home.domain.usecases


import com.example.dailysync.features.home.domain.models.FeedModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchFeedsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
     operator fun invoke(): Flow<Result<List<FeedModel>>> {
        return repository.fetchFeeds()
    }
}
