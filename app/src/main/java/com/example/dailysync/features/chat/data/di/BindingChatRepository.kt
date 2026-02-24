package com.example.dailysync.features.chat.data.di

import com.example.dailysync.features.auth.data.impl.AuthRepositoryImpl
import com.example.dailysync.features.chat.data.impl.ChatRepositoryImpl
import com.example.dailysync.features.chat.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingChatRepository {

    @Binds
    @ViewModelScoped
    abstract fun bindingChatRepository(
        impl: ChatRepositoryImpl
    ): ChatRepository
}
