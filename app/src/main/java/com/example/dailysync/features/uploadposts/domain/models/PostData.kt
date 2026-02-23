package com.example.dailysync.features.uploadposts.domain.models

data class PostData(
    val name: String = "",
    val goals: UserGoalsData = UserGoalsData(),
    val likeNumber: Int = 0
)
