package com.example.dailysync.features.chat.data.mapper

import com.example.dailysync.features.chat.data.model.ChatRoomEntity
import com.example.dailysync.features.chat.data.model.MessageEntity
import com.example.dailysync.features.chat.domain.model.ChatRoom
import com.example.dailysync.features.chat.domain.model.Message

fun ChatRoomEntity.toChatRoom(otherUserName: String, otherUserId: String): ChatRoom {
    return ChatRoom(
        id = id,
        participants = participants,
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        otherUserName = otherUserName,
        otherUserId = otherUserId
    )
}

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        senderId = senderId,
        text = text,
        timestamp = timestamp
    )
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        senderId = senderId,
        text = text,
        timestamp = timestamp
    )
}
