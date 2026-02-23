package com.example.dailysync.features.profile.data.impl

import android.util.Log
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.home.data.mapper.toThoughtModel
import com.example.dailysync.features.home.data.model.ThoughtEntity
import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.profile.data.mapper.toProfileDomain
import com.example.dailysync.features.profile.data.mapper.toProfilePostDomain
import com.example.dailysync.features.profile.data.models.ProfileEntity
import com.example.dailysync.features.profile.data.models.post.ProfilePostEntity
import com.example.dailysync.features.profile.domain.models.ProfileModel
import com.example.dailysync.features.profile.domain.models.post.ProfilePostModel
import com.example.dailysync.features.profile.domain.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepository {


    override fun getCurrentUserId(): Flow<Result<String?>> = callbackFlow {

        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(Result.success(auth.currentUser?.uid))
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override suspend fun toggleFollow(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        if (id.isBlank()) {
            Log.e("FollowError", "Id empty")
            return@withContext Result.failure(AppExceptions.Profile.InvalidId)
        }

        return@withContext try {
            val currentUserId = auth.currentUser?.uid
                ?: return@withContext Result.failure(AppExceptions.Auth.UserNotLoggedIn)
            val db = FirebaseFirestore.getInstance()
            val targetUserRef = db.collection("users").document(id)
            val currentUserRef = db.collection("users").document(currentUserId)

            val currentUserDoc = currentUserRef.get().await()
            val followingIds = currentUserDoc.get("followingIds") as? List<String> ?: emptyList()

            if (followingIds.contains(id)) {
                targetUserRef.update("followers", FieldValue.arrayRemove(currentUserId)).await()
                currentUserRef.update("followingIds", FieldValue.arrayRemove(id)).await()
            } else {
                targetUserRef.update("followers", FieldValue.arrayUnion(currentUserId)).await()
                currentUserRef.update("followingIds", FieldValue.arrayUnion(id)).await()

                val currentUserName = currentUserDoc.getString("name") ?: ""

                val notification = hashMapOf(
                    "userId" to id,
                    "type" to "FOLLOW",
                    "actorId" to currentUserId,
                    "actorName" to currentUserName,
                    "targetId" to "",
                    "timestamp" to System.currentTimeMillis(),
                    "isRead" to false
                )

                targetUserRef
                    .collection("notifications")
                    .add(notification)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FollowError", "Error: ${e.message}")
            Result.failure(AppExceptions.Unknown)
        }
    }

    override suspend fun getUserPosts(userId: String): Result<List<ProfilePostModel>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val postsSnapshot = fireStore.collection("posts")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val posts = postsSnapshot.documents.mapNotNull { document ->
                    try {
                        val postId = document.id
                        val name = document.getString("name") ?: ""
                        val userId = document.getString("userId") ?: ""
                        val likeNumber = document.getLong("likeNumber")?.toInt() ?: 0
                        val releaseDate = document.getLong("releaseDate") ?: 0L

                        val goalsData = document.get("goals") as? Map<String, Any>
                        val goalsList =
                            goalsData?.get("goals") as? List<Map<String, Any>> ?: emptyList()

                        val goals = goalsList.map { goalMap ->
                            com.example.dailysync.features.profile.data.models.post.ProfileGoalsInfoEntity(
                                goal = goalMap["goal"] as? String ?: "",
                                id = goalMap["id"] as? String ?: "",
                                timeRange = goalMap["timeRange"] as? String ?: "",
                                target = goalMap["target"] as? String ?: "",
                                isCompleted = goalMap["isCompleted"] as? Boolean ?: false
                            )
                        }

                        ProfilePostEntity(
                            postId = postId,
                            name = name,
                            userId = userId,
                            goals = com.example.dailysync.features.profile.data.models.post.ProfileGoalsEntity(
                                goals = goals
                            ),
                            likeNumber = likeNumber,
                            releaseDate = releaseDate
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                Result.success(posts.map { it.toProfilePostDomain() })
            } catch (e: Exception) {
                Result.failure(AppExceptions.Unknown)
            }
        }

    override suspend fun getUserThoughts(userId: String): Result<List<ThoughtModel>> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val thoughtsSnapshot = fireStore.collection("thoughts")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val thoughts = mutableListOf<ThoughtEntity>()
                for (document in thoughtsSnapshot.documents) {
                    val thought = document.toObject(ThoughtEntity::class.java)
                    if (thought != null) {
                        thoughts.add(thought.copy(thoughtId = document.id))
                    }
                }

                Result.success(thoughts.map { it.toThoughtModel() })
            } catch (e: Exception) {
                Result.failure(AppExceptions.Unknown)
            }
        }

    override suspend fun toggleGoalCompletion(postId: String, goalId: String): Result<Unit>
         {
            return try {
                val postDoc = fireStore.collection("posts").document(postId).get().await()
                val goalsList = postDoc.get("goals.goals") as? List<Map<String, Any>> ?: emptyList()

                val updatedGoals = goalsList.map { goalMap ->
                    if (goalMap["id"] == goalId) {
                        val currentCompleted = goalMap["isCompleted"] as? Boolean ?: false
                        goalMap.toMutableMap().apply {
                            put("isCompleted", !currentCompleted)
                        }
                    } else {
                        goalMap
                    }
                }

                fireStore.collection("posts")
                    .document(postId)
                    .update("goals.goals", updatedGoals)
                    .await()

                val feedQuery = fireStore.collection("feeds")
                    .whereEqualTo("targetId", postId)
                    .get()
                    .await()

                if (!feedQuery.isEmpty) {
                    val feedDoc = feedQuery.documents.first()
                    fireStore.collection("feeds")
                        .document(feedDoc.id)
                        .update("goals.goals", updatedGoals)
                        .await()
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(AppExceptions.Unknown)
            }
        }
}

