package it.stamp.model.notification

import it.stamp.model.ids.NotificationId
import it.stamp.model.ids.UserId
import kotlin.time.Instant

data class Notification(
    val id: NotificationId,
    val receipientId: UserId,
    val type: NotificationType,
    val title: String,
    val body: String,
    val isRead: Boolean = false,
    val createdAt: Instant,
)
