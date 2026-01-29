package com.example.dailysync.features.home.domain.model

data class PostModel(
    val userName: String,
    val goals: GoalsModel,
    val likeNumber: Int
)