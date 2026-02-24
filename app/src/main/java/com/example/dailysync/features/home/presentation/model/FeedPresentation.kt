package com.example.dailysync.features.home.presentation.model



data class FeedPresentation(
    val id: String = "",
    val targetId: String = "",
    val type: String = "",
    val name: String = "",
    val userId: String = "",
    val likeNumber: Int = 0,
    val releaseTime: Long = 0L,
    val thoughtContent: String = "",
    val goals: GoalsPresentation? = GoalsPresentation(emptyList())

)