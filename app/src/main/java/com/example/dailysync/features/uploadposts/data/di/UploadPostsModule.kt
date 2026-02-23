package com.example.dailysync.features.uploadposts.data.di

import com.example.dailysync.features.uploadposts.domain.usecases.UploadPostsUseCase
import com.example.dailysync.features.uploadposts.domain.usecases.UploadPostsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UploadPostsModule {

    @Provides
    @ViewModelScoped
    fun provideUploadPostsUseCases(
        uploadPostsUseCase: UploadPostsUseCase
    ): UploadPostsUseCases {
        return UploadPostsUseCases(
            uploadPostsUseCase = uploadPostsUseCase
        )
    }
}
