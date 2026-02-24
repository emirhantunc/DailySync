package com.example.dailysync.features.uploadposts.data.mapper

import com.example.dailysync.features.uploadposts.data.model.PostDataEntity
import com.example.dailysync.features.uploadposts.data.model.UserGoalEntity
import com.example.dailysync.features.uploadposts.data.model.UserGoalsDataEntity
import com.example.dailysync.features.uploadposts.domain.models.PostData
import com.example.dailysync.features.uploadposts.domain.models.UserGoal
import com.example.dailysync.features.uploadposts.domain.models.UserGoalsData

fun UserGoalEntity.toUserGoal(): UserGoal {
    return UserGoal(
        goal = goal,
        id = id,
        timeRange = timeRange,
        isCompleted = isCompleted
    )
}

fun List<UserGoalEntity>.toUserGoalList(): List<UserGoal> {
    return this.map { it.toUserGoal() }
}

fun UserGoal.toUserGoalEntity(): UserGoalEntity {
    return UserGoalEntity(
        goal = goal,
        id = id,
        timeRange = timeRange,
        isCompleted = isCompleted
    )
}

fun List<UserGoal>.toUserGoalEntityList(): List<UserGoalEntity> {
    return this.map { it.toUserGoalEntity() }
}

fun UserGoalsDataEntity.toUserGoalsData(): UserGoalsData {
    return UserGoalsData(
        goals = goals.toUserGoalList()
    )
}

fun UserGoalsData.toUserGoalsDataEntity(): UserGoalsDataEntity {
    return UserGoalsDataEntity(
        goals = goals.toUserGoalEntityList()
    )
}

fun PostDataEntity.toPostData(): PostData {
    return PostData(
        name = name,
        goals = goals.toUserGoalsData(),
        likeNumber = likeNumber
    )
}

fun PostData.toPostDataEntity(): PostDataEntity {
    return PostDataEntity(
        name = name,
        goals = goals.toUserGoalsDataEntity(),
        likeNumber = likeNumber
    )
}
