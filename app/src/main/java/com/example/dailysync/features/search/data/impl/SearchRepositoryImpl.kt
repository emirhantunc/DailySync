package com.example.dailysync.features.search.data.impl

import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.search.data.mapper.toSearchModelList
import com.example.dailysync.features.search.data.models.SearchEntity
import com.example.dailysync.features.search.domain.models.SearchModel
import com.example.dailysync.features.search.domain.repository.SearchRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SearchRepositoryImpl@Inject constructor(
    private val fireStore: FirebaseFirestore


): SearchRepository {

    override suspend fun searchUser(name: String): Result<List<SearchModel>> = withContext(Dispatchers.IO) {
        return@withContext try {

            val snapshot = fireStore.collection("users")
                .whereGreaterThanOrEqualTo("name", name)
                .whereLessThanOrEqualTo("name", name + "\uf8ff")
                .get()
                .await()

            val userList = snapshot.toObjects(SearchEntity::class.java).toSearchModelList()

            Result.success(userList)

        } catch (e: FirebaseNetworkException) {
            Result.failure(AppExceptions.Network.NoInternet)
        }
    }
}