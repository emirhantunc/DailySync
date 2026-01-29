package com.example.dailysync.features.search.data.impl

import com.example.dailysync.features.search.data.mapper.toSearchEntity
import com.example.dailysync.features.search.data.mapper.toSearchModelList
import com.example.dailysync.features.search.data.model.SearchEntity
import com.example.dailysync.features.search.domain.exception.SearchError
import com.example.dailysync.features.search.domain.model.SearchModel
import com.example.dailysync.features.search.domain.repository.SearchRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class SearchRepositoryImpl@Inject constructor(
    private val fireStore: FirebaseFirestore


): SearchRepository {



    override suspend fun searchUser(name: String): Result<List<SearchModel>> {
        return try {

            val snapshot = fireStore.collection("users")
                .whereGreaterThanOrEqualTo("name", name)
                .whereLessThanOrEqualTo("name", name + "\uf8ff")
                .get()
                .await()

            val userList = snapshot.toObjects(SearchEntity::class.java).toSearchModelList()

            Result.success(userList)

        } catch (e: FirebaseNetworkException) {
            Result.failure(SearchError.NetworkError)
        }
    }
}