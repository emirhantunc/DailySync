package com.example.dailysync.features.uploadposts.data.impl

import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.uploadposts.data.mapper.toPostDataEntity
import com.example.dailysync.features.uploadposts.data.mapper.toUserGoal
import com.example.dailysync.features.uploadposts.data.model.UserGoalEntity
import com.example.dailysync.features.uploadposts.domain.models.PostData
import com.example.dailysync.features.uploadposts.domain.models.UserGoal
import com.example.dailysync.features.uploadposts.domain.models.UserGoalsData
import com.example.dailysync.features.uploadposts.domain.repository.UploadPostsRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadPostsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UploadPostsRepository {

    override suspend fun uploadPosts(selectedGoals: List<UserGoal>): Result<Unit> =
        withContext(Dispatchers.Default) {
            return@withContext try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(AppExceptions.Auth.UserNotLoggedIn)

                val userDoc = firestore.collection("users").document(userId).get().await()
                val userName = userDoc.getString("name") ?: ""

                val postData = PostData(
                    name = userName,
                    goals = UserGoalsData(
                        goals = selectedGoals
                    ),
                    likeNumber = 0
                )

                val postEntity = postData.toPostDataEntity()
                val releaseTime = System.currentTimeMillis()

                val goalsMap = mapOf(
                    "goals" to postEntity.goals.goals.map { goal ->
                        mapOf(
                            "goal" to goal.goal,
                            "id" to goal.id,
                            "timeRange" to goal.timeRange,
                            "isCompleted" to goal.isCompleted
                        )
                    },
                )

                val postMap = mapOf(
                    "name" to postEntity.name,
                    "userId" to userId,
                    "goals" to goalsMap,
                    "likeNumber" to postEntity.likeNumber,
                    "releaseTime" to releaseTime
                )

                val postDocRef = firestore.collection("posts")
                    .add(postMap)
                    .await()

                val feedDocRef = firestore.collection("feeds")
                    .document()

                val feedMap = mapOf(
                    "targetId" to postDocRef.id,
                    "type" to "POST",
                    "name" to postEntity.name,
                    "userId" to userId,
                    "likeNumber" to postEntity.likeNumber,
                    "releaseTime" to releaseTime,
                    "thoughtContent" to "",
                    "goals" to goalsMap
                )

                feedDocRef.set(feedMap).await()

                Result.success(Unit)
            } catch (e: FirebaseNetworkException) {
                Result.failure(AppExceptions.Network.NoInternet)
            } catch (e: Exception) {
                Result.failure(AppExceptions.Unknown)
            }
        }
}
