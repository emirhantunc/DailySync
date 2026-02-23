package com.example.dailysync.features.auth.domain.usecases

import com.example.dailysync.features.auth.domain.models.SignInModel
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class SingInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
     suspend operator fun invoke(model: SignInModel): Result<Unit>{
        return repository.singIn(model = model)
    }
}