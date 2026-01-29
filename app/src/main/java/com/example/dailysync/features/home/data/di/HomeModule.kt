package com.example.dailysync.features.home.data.di

import com.example.dailysync.features.home.domain.usecases.FetchUseCase
import com.example.dailysync.features.home.domain.usecases.HomeUseCases
import com.example.dailysync.features.home.domain.usecases.ShareGoalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideHomeUseCase(
        fetchUseCase: FetchUseCase,
        shareGoalUseCase: ShareGoalUseCase
    ): HomeUseCases {
        return HomeUseCases(fetchUseCase = fetchUseCase, shareGoalUseCase = shareGoalUseCase)
    }
}