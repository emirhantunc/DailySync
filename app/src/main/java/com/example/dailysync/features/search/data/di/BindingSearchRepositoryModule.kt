package com.example.dailysync.features.search.data.di

import com.example.dailysync.features.search.data.impl.SearchRepositoryImpl
import com.example.dailysync.features.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class BindingSearchRepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindingSearchRepository(impl: SearchRepositoryImpl): SearchRepository


}