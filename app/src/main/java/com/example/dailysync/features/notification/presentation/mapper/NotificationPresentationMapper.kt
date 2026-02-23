package com.example.dailysync.features.notification.presentation.mapper

import com.example.dailysync.features.notification.domain.models.NotificationDomain
import com.example.dailysync.features.notification.presentation.models.NotificationPresentation

fun NotificationDomain.toNotificationPresentation(): NotificationPresentation {
    return NotificationPresentation(
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

fun NotificationPresentation.toNotificationDomain(): NotificationDomain {
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

fun List<NotificationDomain>.toNotificationPresentationList(): List<NotificationPresentation> {
    return this.map { it.toNotificationPresentation() }
}
