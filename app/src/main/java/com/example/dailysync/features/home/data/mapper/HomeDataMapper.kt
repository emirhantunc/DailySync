package com.example.dailysync.features.home.data.mapper

import com.example.dailysync.features.home.data.model.GoalInfoEntity
import com.example.dailysync.features.home.data.model.GoalsEntity
import com.example.dailysync.features.home.data.model.PostEntity
import com.example.dailysync.features.home.domain.model.GoalInfo
import com.example.dailysync.features.home.domain.model.GoalsModel
import com.example.dailysync.features.home.domain.model.PostModel



fun GoalsEntity.toGoalModel(): GoalsModel {
    return GoalsModel(
        goals = goals.toGoalInfoList(),
        completedGoals = completedGoals
    )
}

fun GoalInfoEntity.toGoalInfo(): GoalInfo {
    return GoalInfo(
        goal = goal,
        id = id
    )
}

fun List<GoalInfoEntity>.toGoalInfoList(): List<GoalInfo> {
    return this.map { it.toGoalInfo() }
}


fun PostEntity.toPostModel(): PostModel {
    return PostModel(
        userName = userName,
        goals = goals.toGoalModel(),
        likeNumber = likeNumber
    )
}
fun List<PostEntity>.toPostEntityList(): List<PostModel> {
    return this.map { it.toPostModel() }
}

fun PostModel.toPostEntity(): PostEntity {
    return PostEntity(
        userName = userName,
        goals = goals.toGoalsEntity(),
        likeNumber = likeNumber
    )
}

fun GoalsModel.toGoalsEntity(): GoalsEntity {
    return GoalsEntity(
        goals = goals.toGoalInfoEntityList(),
        completedGoals = completedGoals
    )
}


fun GoalInfo.toGoalInfoEntity(): GoalInfoEntity {
    return GoalInfoEntity(
        goal = goal,
        id = id
    )
}

fun List<GoalInfo>.toGoalInfoEntityList(): List<GoalInfoEntity> {
    return this.map { it.toGoalInfoEntity() }
}


