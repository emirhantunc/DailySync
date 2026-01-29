package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.features.home.domain.model.PostModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class FetchUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() : Result<List<PostModel>>{
        return repository.fetchGoals()
    }

}