package com.example.dailysync.features.home.domain.models

data class GoalInfo(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val isCompleted: Boolean = false
)