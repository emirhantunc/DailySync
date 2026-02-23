package com.example.dailysync.features.profile.data.di

import com.example.dailysync.features.profile.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {
    @Provides
    @ViewModelScoped
    fun provideProfileUseCases(
        getProfileUseCase: GetProfileUseCase,
        toggleFollowUseCase: ToggleFollowUseCase,
        getUserPostsUseCase: GetUserPostsUseCase,
        getUserThoughtsUseCase: GetUserThoughtsUseCase,
        toggleGoalCompletionUseCase: ToggleGoalCompletionUseCase,
        getOwnIdUseCase: GetOwnIdUseCase,
        signOutUseCase: SignOutUseCase
    ): ProfileUseCases {
        return ProfileUseCases(
            getProfileUseCase = getProfileUseCase,
            toggleFollowUseCase = toggleFollowUseCase,
            getUserPostsUseCase = getUserPostsUseCase,
            getUserThoughtsUseCase = getUserThoughtsUseCase,
            toggleGoalCompletionUseCase = toggleGoalCompletionUseCase,
            getOwnIdUseCase = getOwnIdUseCase,
            signOutUseCase = signOutUseCase
        )
    }
}

