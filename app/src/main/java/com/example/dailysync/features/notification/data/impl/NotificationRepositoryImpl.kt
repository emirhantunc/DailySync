package com.example.dailysync.features.notification.data.impl

import android.util.Log
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.notification.data.mapper.toNotificationDomain
import com.example.dailysync.features.notification.data.mapper.toNotificationDomainList
import com.example.dailysync.features.notification.data.mapper.toNotificationEntity
import com.example.dailysync.features.notification.data.models.NotificationEntity
import com.example.dailysync.features.notification.domain.models.NotificationDomain
import com.example.dailysync.features.notification.domain.repository.NotificationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NotificationRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : NotificationRepository {

    override fun getNotifications(): Flow<Result<List<NotificationDomain>>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Result.failure(AppExceptions.Auth.UserNotLoggedIn))
            close()
            return@callbackFlow
        }

        val registration = firestore.collection("users")
            .document(userId)
            .collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(AppExceptions.Network.NoInternet))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val notifications = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(NotificationEntity::class.java)?.copy(id = doc.id)
                    }
                    trySend(Result.success(notifications.toNotificationDomainList()))
                } else {
                    trySend(Result.failure(AppExceptions.Unknown))
                }
            }

        awaitClose { registration.remove() }
    }.flowOn(Dispatchers.IO)

    override suspend fun createNotification(notification: NotificationDomain): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val entity = notification.toNotificationEntity()
            firestore.collection("users")
                .document(notification.userId)
                .collection("notifications")
                .add(entity)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AppExceptions.Network.NoInternet)
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val userId = auth.currentUser?.uid
                ?: return@withContext Result.failure(AppExceptions.Auth.UserNotLoggedIn)

            Log.d("NotificationRepo", "Marking notification $notificationId as read for user $userId")

            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .update("isRead", true)
                .await()

            Log.d("NotificationRepo", "Successfully marked notification $notificationId as read")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NotificationRepo", "Error marking notification as read: ${e.message}")
            Result.failure(AppExceptions.Network.NoInternet)
        }
    }

    override suspend fun getUnreadCount(): Flow<Result<Int>> = callbackFlow {
        val userId = auth.currentUser?.uid
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
    }.flowOn(Dispatchers.IO)
}
