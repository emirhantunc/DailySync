package com.example.dailysync.features.auth.data.di

import com.example.dailysync.features.auth.domain.usecases.AuthUseCases
import com.example.dailysync.features.auth.domain.usecases.SingInUseCase
import com.example.dailysync.features.auth.domain.usecases.SingUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object ProvideUseCases {


    @Provides
    @ViewModelScoped
    fun provideAuthUseCases(
        singUpUseCase: SingUpUseCase,
        singInUseCase: SingInUseCase
    ): AuthUseCases {
        return AuthUseCases(
            singInUseCase = singInUseCase,
            singUpUseCase = singUpUseCase
        )
    }

}