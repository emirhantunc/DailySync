package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.signOut()
    }
}