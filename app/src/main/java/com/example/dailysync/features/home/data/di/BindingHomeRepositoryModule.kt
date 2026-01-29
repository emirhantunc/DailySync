package com.example.dailysync.features.home.data.di

import com.example.dailysync.features.home.data.impl.HomeRepositoryImpl
import com.example.dailysync.features.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingHomeRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindingHomeRepository(impl: HomeRepositoryImpl): HomeRepository

}