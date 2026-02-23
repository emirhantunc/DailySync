package com.example.dailysync.features.notification.data.mapper

import com.example.dailysync.features.notification.data.models.NotificationEntity
import com.example.dailysync.features.notification.domain.models.NotificationDomain

fun NotificationEntity.toNotificationDomain(): NotificationDomain {
    return NotificationDomain(
        id = id,
        userId = userId,
        type = type,
        actorId = actorId,
        actorName = actorName,
        targetId = targetId,
        timestamp = timestamp,
        isRead = isRead
    )
}

fun NotificationDomain.toNotificationEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        userId = userId,
        type = type,
        actorId = actorId,
        actorName = actorName,
        targetId = targetId,
        timestamp = timestamp,
        isRead = isRead
    )
}

fun List<NotificationEntity>.toNotificationDomainList(): List<NotificationDomain> {
    return this.map { it.toNotificationDomain() }
}
