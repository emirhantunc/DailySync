package com.example.dailysync.features.auth.data.impl

import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.auth.data.mapper.toSingInEntity
import com.example.dailysync.features.auth.data.mapper.toSingUpEntity
import com.example.dailysync.features.auth.domain.models.SignInModel
import com.example.dailysync.features.auth.domain.models.SignUpModel
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {


    override suspend fun singIn(model: SignInModel): Result<Unit> = withContext(Dispatchers.IO) {
        val entitySingInModel = model.toSingInEntity()

        return@withContext try {
            auth.signInWithEmailAndPassword(entitySingInModel.email, entitySingInModel.password)
                .await()
            Result.success(Unit)

        } catch (e: FirebaseNetworkException) {
            Result.failure(AppExceptions.Network.NoInternet)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AppExceptions.Auth.InvalidCredentials)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AppExceptions.Auth.AccountNotFound)
        } catch (e: Exception) {
            Result.failure(AppExceptions.Unknown)
        }
    }

    override suspend fun singUp(model: SignUpModel): Result<Unit> = withContext(Dispatchers.IO) {
        val entitySingUpModel = model.toSingUpEntity()

        return@withContext try {
            val authResult = auth.createUserWithEmailAndPassword(
                entitySingUpModel.email,
                entitySingUpModel.password
            ).await()
            val userId = authResult.user?.uid
                ?: return@withContext Result.failure(AppExceptions.Unknown)

            if (userId.isNotEmpty()) {
                val userMap = hashMapOf(
                    "id" to userId,
                    "name" to entitySingUpModel.name,
                    "email" to entitySingUpModel.email,
                    "followers" to entitySingUpModel.followers,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users")
                    .document(userId)
                    .set(userMap)
                    .await()
            }

            Result.success(Unit)

        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(AppExceptions.Auth.InvalidCredentials)
        } catch (e: FirebaseNetworkException) {
            Result.failure(AppExceptions.Network.NoInternet)
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(AppExceptions.Auth.EmailOccupied)
        }
    }
}