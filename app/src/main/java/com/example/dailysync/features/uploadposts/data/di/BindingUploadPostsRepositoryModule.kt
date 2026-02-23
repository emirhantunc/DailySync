package com.example.dailysync.features.uploadposts.data.di

import com.example.dailysync.features.uploadposts.data.impl.UploadPostsRepositoryImpl
import com.example.dailysync.features.uploadposts.domain.repository.UploadPostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingUploadPostsRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindingUploadPostsRepository(impl: UploadPostsRepositoryImpl): UploadPostsRepository
}
