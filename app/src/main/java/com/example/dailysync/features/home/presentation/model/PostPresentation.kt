package com.example.dailysync.features.home.presentation.model

data class PostPresentation(
    val postId: String="",
    val name: String="",
    val userId: String="",
    val goals: GoalsPresentation=GoalsPresentation(),
    val likeNumber: Int=0,
    val releaseDate: Long = 0L
)