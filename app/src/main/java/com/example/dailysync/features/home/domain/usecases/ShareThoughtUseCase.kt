package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class ShareThoughtUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(thought: ThoughtModel): Result<Unit> {
        return repository.shareThought(thought)
    }
}
