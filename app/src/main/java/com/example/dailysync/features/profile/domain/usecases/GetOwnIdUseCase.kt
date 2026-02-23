package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetOwnIdUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<Result<String?>> {
        return repository.getCurrentUserId()
    }
}