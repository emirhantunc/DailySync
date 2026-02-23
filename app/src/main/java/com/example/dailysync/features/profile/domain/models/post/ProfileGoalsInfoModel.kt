package com.example.dailysync.features.profile.domain.models.post

data class ProfileGoalsInfoModel(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val target: String = "",
    val isCompleted: Boolean = false
)