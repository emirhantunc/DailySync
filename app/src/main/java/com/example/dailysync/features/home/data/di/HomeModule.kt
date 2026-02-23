package com.example.dailysync.features.home.data.di

import com.example.dailysync.features.home.domain.usecases.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @Provides
    @ViewModelScoped
    fun provideHomeUseCase(
        getUserProfileUseCase: GetUserProfileUseCase,
        shareThoughtUseCase: ShareThoughtUseCase,
        getLikedFeedsUseCase: GetLikedFeedsUseCase,
        fetchFeedsUseCase: FetchFeedsUseCase,
        likeContentUseCase: LikeContentUseCase,
        unlikeContentUseCase: UnlikeContentUseCase,
        getUnreadNotificationCountUseCase: GetUnreadNotificationCountUseCase
    ): HomeUseCases {
        return HomeUseCases(
            getUserProfileUseCase = getUserProfileUseCase,
            shareThoughtUseCase = shareThoughtUseCase,
            getLikedFeedsUseCase = getLikedFeedsUseCase,
            fetchFeedsUseCase = fetchFeedsUseCase,
            likeContentUseCase = likeContentUseCase,
            unlikeContentUseCase = unlikeContentUseCase,
            getUnreadNotificationCountUseCase = getUnreadNotificationCountUseCase
        )
    }
}
