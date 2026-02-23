package com.example.dailysync.features.home.domain.usecases

data class HomeUseCases(
    val getUserProfileUseCase: GetUserProfileUseCase,
    val shareThoughtUseCase: ShareThoughtUseCase,
    val getLikedFeedsUseCase: GetLikedFeedsUseCase,
    val fetchFeedsUseCase: FetchFeedsUseCase,
    val likeContentUseCase: LikeContentUseCase,
    val unlikeContentUseCase: UnlikeContentUseCase,
    val getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase
)

