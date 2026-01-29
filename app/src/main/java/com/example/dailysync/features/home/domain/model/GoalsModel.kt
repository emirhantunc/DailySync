package com.example.dailysync.features.home.domain.model

data class GoalsModel(
    val goals: List<GoalInfo>,
    val completedGoals: List<String>
)
