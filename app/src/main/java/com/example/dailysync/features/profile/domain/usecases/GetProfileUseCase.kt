package com.example.dailysync.features.profile.domain.usecases

import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import com.example.dailysync.features.profile.domain.models.ProfileModel
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
){
    operator fun invoke(id: String?) : Flow<Result<CoreProfileModel>> {
        return repository.getUserProfile(userId = id)
    }
}