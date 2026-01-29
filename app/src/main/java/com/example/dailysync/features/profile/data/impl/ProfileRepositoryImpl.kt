package com.example.dailysync.features.profile.data.impl

import com.example.dailysync.features.profile.data.mapper.toProfileDomain
import com.example.dailysync.features.profile.data.model.ProfileEntity
import com.example.dailysync.features.profile.domain.exceptions.GetProfileError
import com.example.dailysync.features.profile.domain.model.ProfileDomain
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepository {

    override suspend fun getProfile(id: String?): Result<ProfileDomain> {
        return try {
            val targetId = id ?: auth.currentUser?.uid
            ?: return Result.failure(GetProfileError.UnknownError)
            val snapshot = fireStore.collection("users")
                .document(targetId)
                .get()
                .await()

            if (snapshot.exists()) {
                val profile = snapshot.toObject(ProfileEntity::class.java)?.toProfileDomain()
                    ?: throw Exception("Mapping Error")

                Result.success(profile)
            } else {
                Result.failure(GetProfileError.ProfileNotFound)
            }

        } catch (e: FirebaseNetworkException) {
            return Result.failure(GetProfileError.NetworkError)
        }
    }
}