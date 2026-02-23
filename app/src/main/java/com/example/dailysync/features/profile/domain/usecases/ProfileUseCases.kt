package com.example.dailysync.features.profile.domain.usecases


data class ProfileUseCases(
    val getProfileUseCase: GetProfileUseCase,
    val toggleFollowUseCase: ToggleFollowUseCase,
    val getUserPostsUseCase: GetUserPostsUseCase,
    val getUserThoughtsUseCase: GetUserThoughtsUseCase,
    val toggleGoalCompletionUseCase: ToggleGoalCompletionUseCase,
    val getOwnIdUseCase: GetOwnIdUseCase,
    val signOutUseCase: SignOutUseCase
)

