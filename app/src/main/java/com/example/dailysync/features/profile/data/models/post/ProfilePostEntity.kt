package com.example.dailysync.features.profile.data.models.post


data class ProfilePostEntity(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: ProfileGoalsEntity = ProfileGoalsEntity(),
    val likeNumber: Int = 0,
    val releaseTime: Long = 0L
)