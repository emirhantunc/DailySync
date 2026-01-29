package com.example.dailysync.features.home.data.model

import com.example.dailysync.features.home.domain.model.GoalInfo

data class GoalsEntity(
    val goals: List<GoalInfoEntity>,
    val completedGoals: List<String>
)