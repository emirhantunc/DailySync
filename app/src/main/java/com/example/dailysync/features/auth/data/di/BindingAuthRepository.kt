package com.example.dailysync.features.auth.data.di

import com.example.dailysync.features.auth.data.impl.AuthRepositoryImpl
import com.example.dailysync.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingAuthRepository {


    @Binds
    @ViewModelScoped
    abstract fun bindingAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}