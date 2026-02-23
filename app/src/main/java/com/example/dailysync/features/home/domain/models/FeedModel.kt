package com.example.dailysync.features.home.domain.models

data class FeedModel(
    val id: String = "",
    val feedId: String = "",
    val targetId: String = "",
    val type: String = "",
    val name: String = "",
    val userId: String = "",
    val likeNumber: Int = 0,
    val releaseTime: Long = 0L,
    val thoughtContent: String = "",
    val goals: GoalsModel? = GoalsModel(emptyList())
)