package com.example.dailysync.features.chat.data.di

import com.example.dailysync.features.chat.data.impl.ChatRepositoryImpl
import com.example.dailysync.features.chat.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): ChatRepository {
        return ChatRepositoryImpl(firestore, auth)
    }
}
