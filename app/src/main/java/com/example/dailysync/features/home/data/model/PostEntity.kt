package com.example.dailysync.features.home.data.model

data class PostEntity(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: GoalsEntity = GoalsEntity(),
    val likeNumber: Int = 0,
    val releaseDate: Long = 0L
)