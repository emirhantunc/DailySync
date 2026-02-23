package com.example.dailysync.features.uploadposts.domain.models

data class UserGoal(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val target: String = "",
    val isCompleted: Boolean = false
)
