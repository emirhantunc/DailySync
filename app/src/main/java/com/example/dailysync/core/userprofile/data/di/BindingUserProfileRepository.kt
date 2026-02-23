package com.example.dailysync.core.userprofile.data.di

import com.example.dailysync.core.userprofile.data.impl.UserProfileRepositoryImpl
import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class BindingUserProfileRepository {

    @Binds
    @Singleton
    abstract fun bindingUserProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository

}