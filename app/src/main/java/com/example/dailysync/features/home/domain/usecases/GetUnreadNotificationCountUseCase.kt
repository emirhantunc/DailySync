package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnreadNotificationCountUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Result<Int>> {
        return repository.getUnreadNotificationCount()
    }
}
