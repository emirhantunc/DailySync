package com.example.dailysync.core.userprofile.data.impl

import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.core.userprofile.data.mapper.toProfileModel
import com.example.dailysync.core.userprofile.data.model.CoreProfileEntity
import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserProfileRepository {
    private var cachedCurrentUser: CoreProfileModel? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserProfile(userId: String?): Flow<Result<CoreProfileModel>> {
        val authStateFlow = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                trySend(firebaseAuth.currentUser?.uid)
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }.distinctUntilChanged()

        val targetUserIdFlow = if (!userId.isNullOrBlank()) {
            flowOf(userId)
        } else {
            authStateFlow
        }

        return targetUserIdFlow.flatMapLatest { id ->
            when {
                id.isNullOrBlank() -> flowOf(Result.failure(AppExceptions.Profile.ProfileNotFound))

                id == auth.currentUser?.uid && cachedCurrentUser != null -> {
                    flowOf(Result.success(cachedCurrentUser!!))
                }

                else -> fetchProfileFromFirestore(id)
            }
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            cachedCurrentUser = null
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AppExceptions.Network.NoInternet)
        }
    }

    private fun fetchProfileFromFirestore(userId: String): Flow<Result<CoreProfileModel>> =
        callbackFlow {
            val docRef = firestore.collection("users").document(userId)
            val registration =
                docRef.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(AppExceptions.Network.NoInternet))
                        return@addSnapshotListener
                    }

                    if (snapshot == null || !snapshot.exists()) {
                        trySend(Result.failure(AppExceptions.Profile.ProfileNotFound))
                        return@addSnapshotListener
                    }

                    val entity = snapshot.toObject(CoreProfileEntity::class.java)

                    if (entity != null) {
                        val profileModel = entity.toProfileModel()
                        if (userId == auth.currentUser?.uid) {
                            cachedCurrentUser = profileModel
                        }
                        trySend(Result.success(profileModel))
                    } else {
                        trySend(Result.failure(AppExceptions.MappingError))
                    }
                }
            awaitClose { registration.remove() }
        }.flowOn(Dispatchers.IO)
}