package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.features.home.domain.model.GoalsModel
import com.example.dailysync.features.home.domain.model.PostModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class ShareGoalUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(post: PostModel): Result<Unit>{
       return repository.shareGoal(post = post)
    }
}