package com.example.dailysync.features.profile.data.models.post

data class ProfileGoalsInfoEntity(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val target: String = "",
    val isCompleted : Boolean = false
)