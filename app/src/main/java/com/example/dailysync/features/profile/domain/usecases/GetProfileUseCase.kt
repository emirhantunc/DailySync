package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.features.profile.domain.model.ProfileDomain
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
){
    suspend operator fun invoke(id: String?) : Result<ProfileDomain>{
        return repository.getProfile(id = id)
    }
}