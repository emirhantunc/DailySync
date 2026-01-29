package com.example.dailysync.features.home.data.impl

import com.example.dailysync.features.home.data.mapper.toPostEntity
import com.example.dailysync.features.home.data.mapper.toPostModel
import com.example.dailysync.features.home.data.model.PostEntity
import com.example.dailysync.features.home.domain.exceptions.FetchError
import com.example.dailysync.features.home.domain.exceptions.ShareError
import com.example.dailysync.features.home.domain.model.PostModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : HomeRepository {


    override suspend fun fetchGoals(): Result<List<PostModel>> {
        return try {
            val snapshot = firestore.collection("posts")
                .get()
                .await()
            val entities = snapshot.toObjects(PostEntity::class.java)
            val models = entities.map { it.toPostModel() }
            Result.success(models)

        } catch (e: FirebaseNetworkException) {
            Result.failure(FetchError.NetworkError)
        }
    }

    override suspend fun shareGoal(post: PostModel): Result<Unit> {
        return try {
            firestore.collection("posts")
                .add(post.toPostEntity())
                .await()

            Result.success(Unit)
        } catch (e: FirebaseNetworkException) {
            Result.failure(ShareError.NetworkError)
        }
    }

}