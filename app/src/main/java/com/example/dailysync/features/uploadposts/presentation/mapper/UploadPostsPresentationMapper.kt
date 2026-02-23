package com.example.dailysync.features.uploadposts.presentation.mapper

import com.example.dailysync.features.uploadposts.domain.models.UserGoal
import com.example.dailysync.features.uploadposts.presentation.model.UserGoalPresentation

fun UserGoal.toUserGoalPresentation(): UserGoalPresentation {
    return UserGoalPresentation(
        goal = goal,
        id = id,
        timeRange = timeRange,
        target = target,
        isSelected = false
    )
}

fun List<UserGoal>.toUserGoalPresentationList(): List<UserGoalPresentation> {
    return this.map { it.toUserGoalPresentation() }
}

fun UserGoalPresentation.toUserGoal(): UserGoal {
    return UserGoal(
        goal = goal,
        id = id,
        timeRange = timeRange,
        target = target
    )
}

fun List<UserGoalPresentation>.toUserGoalList(): List<UserGoal> {
    return this.map { it.toUserGoal() }
}
