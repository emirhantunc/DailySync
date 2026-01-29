package com.example.dailysync.features.profile.domain.repository

import com.example.dailysync.features.profile.domain.model.ProfileDomain


interface ProfileRepository {
    suspend fun getProfile(id: String? = null): Result<ProfileDomain>
}