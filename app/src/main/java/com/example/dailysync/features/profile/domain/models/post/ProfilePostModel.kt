package com.example.dailysync.features.profile.domain.models.post


data class ProfilePostModel(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: ProfileGoalsModel = ProfileGoalsModel(),
    val likeNumber: Int = 0,
    val releaseDate: Long = 0L
)