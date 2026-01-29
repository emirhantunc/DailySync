package com.example.dailysync.features.home.presentation.model

import com.example.dailysync.features.home.domain.model.GoalInfo

data class GoalsPresentation(
    val goals: List<GoalInfoPresentation>,
    val completedGoals: List<String>
)