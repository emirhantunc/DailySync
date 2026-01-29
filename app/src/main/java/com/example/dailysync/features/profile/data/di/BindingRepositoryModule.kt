package com.example.dailysync.features.profile.data.di

import com.example.dailysync.features.profile.data.impl.ProfileRepositoryImpl
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindingRepositoryModule(impl: ProfileRepositoryImpl): ProfileRepository
}