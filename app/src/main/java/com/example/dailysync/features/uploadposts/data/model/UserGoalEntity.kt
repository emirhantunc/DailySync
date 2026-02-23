package com.example.dailysync.features.uploadposts.data.model

data class UserGoalEntity(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val target: String = "",
    val isCompleted : Boolean = false
)
