package com.example.dailysync.features.search.domain.usecases

import com.example.dailysync.features.search.domain.models.SearchModel
import com.example.dailysync.features.search.domain.repository.SearchRepository
import jakarta.inject.Inject


class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(name: String): Result<List<SearchModel>> {
        return repository.searchUser(name)
    }
}