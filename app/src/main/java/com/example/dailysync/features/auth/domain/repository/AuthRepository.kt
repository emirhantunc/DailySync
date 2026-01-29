package com.example.dailysync.features.auth.domain.repository

import com.example.dailysync.features.auth.domain.model.SignInModel
import com.example.dailysync.features.auth.domain.model.SignUpModel

interface AuthRepository {
    suspend fun singIn(model: SignInModel): Result<Unit>
    suspend fun singUp(model: SignUpModel): Result<Unit>
}