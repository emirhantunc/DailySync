package com.example.dailysync.features.profile.presentation.models.post


data class ProfilePostPresentation(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: ProfileGoalsPresentation = ProfileGoalsPresentation(),
    val likeNumber: Int = 0,
    val releaseTime: Long = 0L
)