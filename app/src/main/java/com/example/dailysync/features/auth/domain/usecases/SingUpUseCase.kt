package com.example.dailysync.features.auth.domain.usecases

import com.example.dailysync.features.auth.domain.models.SignUpModel
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class SingUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(model: SignUpModel): Result<Unit> {
        return repository.singUp(model)
    }
}