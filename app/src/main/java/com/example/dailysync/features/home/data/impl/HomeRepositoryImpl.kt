package com.example.dailysync.features.home.data.impl

import android.util.Log
import com.example.dailysync.core.enums.FeedType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.home.data.mapper.toFeedModelList
import com.example.dailysync.features.home.data.mapper.toThoughtEntity
import com.example.dailysync.features.home.data.model.FeedEntity
import com.example.dailysync.features.home.domain.models.FeedModel
import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.home.domain.repository.HomeRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : HomeRepository {

    val userIdFlow: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }
    }


    override suspend fun shareThought(thought: ThoughtModel): Result<Unit> =
        withContext(Dispatchers.Default) {
            return@withContext try {
                val thoughtEntity = thought.toThoughtEntity()
                val thoughtMap = mapOf(
                    "thought" to thoughtEntity.thought,
                    "name" to thoughtEntity.name,
                    "userId" to thoughtEntity.userId,
                    "releaseTime" to thoughtEntity.releaseTime,
                    "likeNumber" to thoughtEntity.likeNumber
                )

                val thoughtDocRef = firestore.collection("thoughts")
                    .add(thoughtMap)
                    .await()

                val feedDocRef = firestore.collection("feeds")
                    .document()

                val feedMap = mapOf(
                    "targetId" to thoughtDocRef.id,
                    "type" to "THOUGHT",
                    "name" to thoughtEntity.name,
                    "userId" to thoughtEntity.userId,
                    "likeNumber" to thoughtEntity.likeNumber,
                    "releaseTime" to thoughtEntity.releaseTime,
                    "thoughtContent" to thoughtEntity.thought,
                    "goals" to null
                )

                feedDocRef.set(feedMap).await()

                Result.success(Unit)
            } catch (e: FirebaseNetworkException) {
                Result.failure(AppExceptions.Network.NoInternet)
            } catch (e: Exception) {
                Result.failure(AppExceptions.Unknown)
            }
        }

    override suspend fun getLikedFeeds(): Result<List<String>> {
        return try {
            val userId = userIdFlow.firstOrNull()
                ?: return Result.failure(AppExceptions.Auth.UserNotLoggedIn)

            val snapshot = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val likedFeeds = snapshot.get("likedFeeds") as? List<String> ?: emptyList()
            Result.success(likedFeeds)
        } catch (e: Exception) {
            Result.failure(AppExceptions.Unknown)
        }
    }

    override fun fetchFeeds(): Flow<Result<List<FeedModel>>> = callbackFlow {
        val userId = userIdFlow.firstOrNull()
        if (userId == null) {
            trySend(Result.failure(AppExceptions.Auth.UserNotLoggedIn))
            close()
            return@callbackFlow
        }

        val userDoc = firestore.collection("users").document(userId).get().await()
        val followingIds = userDoc.get("followingIds") as? List<String> ?: emptyList()

        if (followingIds.isEmpty()) {
            trySend(Result.success(emptyList()))
            close()
            return@callbackFlow
        }

        val chunks = followingIds.chunked(10)

        val cachedFeeds = ConcurrentHashMap<Int, List<FeedEntity>>()
        val listeners = mutableListOf<ListenerRegistration>()

        chunks.forEachIndexed { index, chunk ->
            val query = firestore.collection("feeds")
                .whereIn("userId", chunk)
                .orderBy("releaseTime", Query.Direction.DESCENDING)
                .limit(20)

            val registration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("HomeRepo", "Listen error", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val feeds = snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.id
                            val targetId = doc.getString("targetId") ?: ""
                            val type = doc.getString("type") ?: ""
                            val name = doc.getString("name") ?: ""
                            val userId = doc.getString("userId") ?: ""
                            val likeNumber = doc.getLong("likeNumber")?.toInt() ?: 0
                            val releaseTime = doc.getLong("releaseTime") ?: 0L
                            val thoughtContent = doc.getString("thoughtContent") ?: ""

                            val goalsData = doc.get("goals") as? Map<String, Any>
                            val goals = if (goalsData != null) {
                                val goalsList =
                                    goalsData["goals"] as? List<Map<String, Any>> ?: emptyList()
                                val goalInfoEntities = goalsList.map { goalMap ->
                                    com.example.dailysync.features.home.data.model.GoalInfoEntity(
                                        goal = goalMap["goal"] as? String ?: "",
                                        id = goalMap["id"] as? String ?: "",
                                        timeRange = goalMap["timeRange"] as? String ?: "",
                                        isCompleted = goalMap["isCompleted"] as? Boolean ?: false
                                    )
                                }
                                com.example.dailysync.features.home.data.model.GoalsEntity(goals = goalInfoEntities)
                            } else {
                                null
                            }

                            FeedEntity(
                                id = id,
                                targetId = targetId,
                                type = type,
                                name = name,
                                userId = userId,
                                likeNumber = likeNumber,
                                releaseTime = releaseTime,
                                thoughtContent = thoughtContent,
                                goals = goals
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }

                    cachedFeeds[index] = feeds

                    val allFeedsCombined = cachedFeeds.values.flatten()
                        .sortedByDescending { it.releaseTime }


                    trySend(Result.success(allFeedsCombined.toFeedModelList()))
                }
            }
            listeners.add(registration)
        }
        awaitClose {
            listeners.forEach { it.remove() }
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun likeContent(contentId: String, contentType: FeedType): Result<Unit> {
        return try {
            val userId =
                userIdFlow.firstOrNull()
                    ?: return Result.failure(AppExceptions.Auth.UserNotLoggedIn)

            val querySnapshot = firestore.collection("feeds")
                .whereEqualTo("targetId", contentId)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val feedId = document.id
                val postOwnerId = document.getString("userId") ?: ""

                firestore.collection("users")
                    .document(userId)
                    .update("likedFeeds", FieldValue.arrayUnion(feedId))
                    .await()

                firestore.collection("users")
                    .document(userId)
                    .update(contentType.likeFieldName, FieldValue.arrayUnion(contentId))
                    .await()

                document.reference
                    .update("likeNumber", FieldValue.increment(1))
                    .await()

                firestore.collection(contentType.documentName)
                    .document(contentId)
                    .update("likeNumber", FieldValue.increment(1))
                    .await()

                if (postOwnerId != userId && postOwnerId.isNotBlank()) {
                    val currentUserDoc = firestore.collection("users")
                        .document(userId)
                        .get()
                        .await()
                    val currentUserName = currentUserDoc.getString("name") ?: ""

                    val notification = hashMapOf(
                        "userId" to postOwnerId,
                        "type" to "LIKE",
                        "actorId" to userId,
                        "actorName" to currentUserName,
                        "targetId" to contentId,
                        "timestamp" to System.currentTimeMillis(),
                        "isRead" to false
                    )

                    firestore.collection("users")
                        .document(postOwnerId)
                        .collection("notifications")
                        .add(notification)
                        .await()
                }
            }


            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("likeContent", e.message.toString())
            Result.failure(AppExceptions.Unknown)
        }
    }

    override fun getUnreadNotificationCount(): Flow<Result<Int>> = callbackFlow {
        val userId = userIdFlow.firstOrNull()
        if (userId == null) {
            trySend(Result.failure(AppExceptions.Auth.UserNotLoggedIn))
            close()
            return@callbackFlow
        }

        val registration = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .whereEqualTo("isRead", false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(AppExceptions.Network.NoInternet))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    trySend(Result.success(snapshot.size()))
                } else {
                    trySend(Result.failure(AppExceptions.Unknown))
                }
            }

        awaitClose { registration.remove() }
    }


    override suspend fun unlikeContent(
        contentId: String,
        contentType: FeedType
    ): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(AppExceptions.Auth.UserNotLoggedIn)

            val querySnapshot = firestore.collection("feeds")
                .whereEqualTo("targetId", contentId)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val feedId = document.id

                firestore.collection("users")
                    .document(userId)
                    .update("likedFeeds", FieldValue.arrayRemove(feedId))
                    .await()

                firestore.collection("users")
                    .document(userId)
                    .update(contentType.likeFieldName, FieldValue.arrayRemove(contentId))
                    .await()

                document.reference
                    .update("likeNumber", FieldValue.increment(-1))
                    .await()

                firestore.collection(contentType.documentName)
                    .document(contentId)
                    .update("likeNumber", FieldValue.increment(-1))
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AppExceptions.Unknown)
        }
    }
}



