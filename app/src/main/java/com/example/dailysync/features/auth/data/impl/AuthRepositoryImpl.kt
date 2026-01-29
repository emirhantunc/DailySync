package com.example.dailysync.features.auth.data.impl

import com.example.dailysync.features.auth.data.mapper.toSingInEntity
import com.example.dailysync.features.auth.data.mapper.toSingUpEntity
import com.example.dailysync.features.auth.domain.exceptions.SignUpError
import com.example.dailysync.features.auth.domain.exceptions.SingInError
import com.example.dailysync.features.auth.domain.model.SignInModel
import com.example.dailysync.features.auth.domain.model.SignUpModel
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {


    override suspend fun singIn(model: SignInModel): Result<Unit> {
        val entitySingInModel = model.toSingInEntity()

        return try {
            auth.signInWithEmailAndPassword(entitySingInModel.email, entitySingInModel.password).await()
            Result.success(Unit)

        } catch (e: FirebaseNetworkException) {
            Result.failure(SingInError.NetworkError)
        }
    }
    override suspend fun singUp(model: SignUpModel): Result<Unit> {
        val entitySingUpModel = model.toSingUpEntity()

        return try {
            val authResult = auth.createUserWithEmailAndPassword(
                entitySingUpModel.email,
                entitySingUpModel.password
            ).await()

            val userId = authResult.user?.uid
                ?: return Result.failure(SignUpError.UnknownError)
            val userMap = hashMapOf(
                "uid" to userId,
                "email" to entitySingUpModel.email,
                "createdAt" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .set(userMap)
                .await()

            Result.success(Unit)

        }  catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(SignUpError.WeakPassword)
        } catch (e: FirebaseNetworkException) {
            Result.failure(SignUpError.NetworkError)
        }
    }
}