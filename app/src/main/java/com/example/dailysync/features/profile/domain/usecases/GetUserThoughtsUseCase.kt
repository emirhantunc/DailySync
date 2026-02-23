package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserThoughtsUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): Result<List<ThoughtModel>> {
        return repository.getUserThoughts(userId)
    }
}
