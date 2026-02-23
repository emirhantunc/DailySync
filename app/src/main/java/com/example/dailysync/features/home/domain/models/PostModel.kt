package com.example.dailysync.features.home.domain.models

data class PostModel(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: GoalsModel = GoalsModel(),
    val likeNumber: Int = 0,
    val releaseDate: Long = 0L
)