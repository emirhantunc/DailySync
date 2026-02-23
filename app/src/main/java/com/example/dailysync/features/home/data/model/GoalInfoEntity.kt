package com.example.dailysync.features.home.data.model

data class GoalInfoEntity(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val target: String = "",
    val isCompleted: Boolean = false
)