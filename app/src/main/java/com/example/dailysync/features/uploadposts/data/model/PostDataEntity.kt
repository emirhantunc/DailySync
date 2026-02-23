package com.example.dailysync.features.uploadposts.data.model

data class PostDataEntity(
    val name: String = "",
    val goals: UserGoalsDataEntity = UserGoalsDataEntity(),
    val likeNumber: Int = 0
)
