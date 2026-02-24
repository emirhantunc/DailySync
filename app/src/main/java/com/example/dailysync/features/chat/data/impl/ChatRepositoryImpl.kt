package com.example.dailysync.features.chat.data.impl

import android.util.Log
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.chat.data.mapper.toChatRoom
import com.example.dailysync.features.chat.data.mapper.toMessage
import com.example.dailysync.features.chat.data.model.ChatRoomEntity
import com.example.dailysync.features.chat.data.model.MessageEntity
import com.example.dailysync.features.chat.domain.model.ChatRoom
import com.example.dailysync.features.chat.domain.model.FollowingUser
import com.example.dailysync.features.chat.domain.model.Message
import com.example.dailysync.features.chat.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ChatRepository {

    override fun getChatRooms(): Flow<Result<List<ChatRoom>>> = callbackFlow {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            trySend(Result.failure(AppExceptions.Auth.UserNotLoggedIn))
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("chatRooms")
            .whereArrayContains("participants", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(AppExceptions.Unknown))
                    return@addSnapshotListener
                }

                this.launch {
                    if (snapshot != null) {
                        val chatRooms = snapshot.documents.mapNotNull { doc ->
                            try {
                                val id = doc.id
                                val participants =
                                    doc.get("participants") as? List<String> ?: emptyList()
                                val lastMessage = doc.getString("lastMessage") ?: ""
                                val lastMessageTime = doc.getLong("lastMessageTime") ?: 0L

                                val otherUserId = participants.firstOrNull { it != userId }
                                    ?: return@mapNotNull null

                                val otherUserDoc = firestore.collection("users")
                                    .document(otherUserId)
                                    .get()
                                    .await()

                                val otherUserName = otherUserDoc.getString("name") ?: ""

                                ChatRoomEntity(
                                    id = id,
                                    participants = participants,
                                    lastMessage = lastMessage,
                                    lastMessageTime = lastMessageTime
                                ).toChatRoom(otherUserName, otherUserId)
                            } catch (e: Exception) {
                                Log.e("ChatRepo", "Error parsing chat room", e)
                                null
                            }
                        }
                        trySend(Result.success(chatRooms))
                    }
                }

            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    override fun getMessages(chatRoomId: String): Flow<Result<List<Message>>> = callbackFlow {
        val listener = firestore.collection("chatRooms")
            .document(chatRoomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(AppExceptions.Unknown))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            MessageEntity(
                                id = doc.id,
                                senderId = doc.getString("senderId") ?: "",
                                text = doc.getString("text") ?: "",
                                timestamp = doc.getLong("timestamp") ?: 0L
                            ).toMessage()
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(Result.success(messages))
                }
            }

        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    override suspend fun sendMessage(chatRoomId: String, text: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(AppExceptions.Auth.UserNotLoggedIn)

                val timestamp = System.currentTimeMillis()

                val message = hashMapOf(
                    "senderId" to userId,
                    "text" to text,
                    "timestamp" to timestamp
                )

                firestore.collection("chatRooms")
                    .document(chatRoomId)
                    .collection("messages")
                    .add(message)
                    .await()

                firestore.collection("chatRooms")
                    .document(chatRoomId)
                    .update(
                        mapOf(
                            "lastMessage" to text,
                            "lastMessageTime" to timestamp
                        )
                    )
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Log.e("ChatRepo", "Error sending message", e)
                Result.failure(AppExceptions.Unknown)
            }
        }

    override suspend fun createOrGetChatRoom(otherUserId: String): Result<String> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val userId = auth.currentUser?.uid
                    ?: return@withContext Result.failure(AppExceptions.Auth.UserNotLoggedIn)

                val participants = listOf(userId, otherUserId).sorted()

                val existingRoom = firestore.collection("chatRooms")
                    .whereArrayContains("participants", userId)
                    .get()
                    .await()
                    .documents
                    .firstOrNull { doc ->
                        val roomParticipants =
                            (doc.get("participants") as? List<String>)?.sorted() ?: emptyList()
                        roomParticipants == participants
                    }

                if (existingRoom != null) {
                    Result.success(existingRoom.id)
                } else {
                    val newRoom = hashMapOf(
                        "participants" to participants,
                        "lastMessage" to "",
                        "lastMessageTime" to System.currentTimeMillis()
                    )

                    val docRef = firestore.collection("chatRooms")
                        .add(newRoom)
                        .await()

                    Result.success(docRef.id)
                }
            } catch (e: Exception) {
                Log.e("ChatRepo", "Error creating chat room", e)
                Result.failure(AppExceptions.Unknown)
            }
        }

    override suspend fun getFollowingUsers(): Result<List<FollowingUser>> {
        return try {
            val userId = auth.currentUser?.uid
                ?: return Result.failure(AppExceptions.Auth.UserNotLoggedIn)

            val userDoc = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            val followingIds = userDoc.get("followingIds") as? List<String> ?: emptyList()

            val followingUsers = followingIds.mapNotNull { followingId ->
                try {
                    val followingUserDoc = firestore.collection("users")
                        .document(followingId)
                        .get()
                        .await()

                    val name = followingUserDoc.getString("name") ?: ""
                    FollowingUser(id = followingId, name = name)
                } catch (e: Exception) {
                    Log.e("ChatRepo", "Error fetching following user", e)
                    null
                }
            }

            Result.success(followingUsers)
        } catch (e: Exception) {
            Log.e("ChatRepo", "Error getting following users", e)
            Result.failure(AppExceptions.Unknown)
        }

    }
}
