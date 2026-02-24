package com.example.dailysync.features.home.data.model

import com.example.dailysync.core.enums.FeedType


data class FeedEntity(
    val id: String = "",
    val targetId: String = "",
    val type: String = "",
    val name: String = "",
    val userId: String = "",
    val likeNumber: Int = 0,
    val releaseTime: Long = 0L,
    val thoughtContent: String = "",
    val goals: GoalsEntity? = GoalsEntity(emptyList())
)
