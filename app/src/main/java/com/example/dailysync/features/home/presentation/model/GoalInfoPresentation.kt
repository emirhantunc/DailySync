package com.example.dailysync.features.home.presentation.model

data class GoalInfoPresentation(
    val goal: String = "",
    val id: String = "",
    val timeRange: String = "",
    val isChecked: Boolean = true,
    val isCompleted: Boolean = false
)