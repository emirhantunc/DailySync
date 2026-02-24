package com.example.dailysync.features.profile.presentation.models.post

data class ProfileGoalsInfoPresentation(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val isCompleted: Boolean = false
)