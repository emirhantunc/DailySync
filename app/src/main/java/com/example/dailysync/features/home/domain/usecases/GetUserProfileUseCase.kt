package com.example.dailysync.features.home.domain.usecases

import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import com.example.dailysync.features.home.domain.models.UserProfile
import com.example.dailysync.features.home.domain.repository.HomeRepository
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    operator fun invoke(): Flow<Result<CoreProfileModel>> {
        return repository.getUserProfile(null)
    }
}
