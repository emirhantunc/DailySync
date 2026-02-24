package com.example.dailysync.core.userprofile.domain.repository

import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.features.home.domain.models.UserProfile
import com.example.dailysync.features.profile.data.models.ProfileEntity
import kotlinx.coroutines.flow.Flow


interface UserProfileRepository {
    fun getUserProfile(userId: String?): Flow<Result<CoreProfileModel>>

    fun getCurrentUserId():Flow<Result<String?>>

    suspend fun signOut(): Result<Unit>
}
