package com.example.dailysync.features.search.domain.repository

import com.example.dailysync.features.search.domain.model.SearchModel

interface SearchRepository {
    suspend fun searchUser(name: String): Result<List<SearchModel>>
}