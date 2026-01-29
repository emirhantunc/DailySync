package com.example.dailysync.features.home.presentation.mapper

import com.example.dailysync.features.home.domain.model.GoalInfo
import com.example.dailysync.features.home.domain.model.GoalsModel
import com.example.dailysync.features.home.domain.model.PostModel
import com.example.dailysync.features.home.presentation.model.GoalInfoPresentation
import com.example.dailysync.features.home.presentation.model.GoalsPresentation
import com.example.dailysync.features.home.presentation.model.PostPresentation


fun PostModel.toPostPresentation(): PostPresentation {
    return PostPresentation(
        userName = userName,
        goals = goals.toGoalsPresentation(),
        likeNumber = likeNumber
    )
}

fun List<PostModel>.toPostPresentationList(): List<PostPresentation> {
    return this.map { it.toPostPresentation() }
}


fun GoalInfo.toGoalInfoPresentation(): GoalInfoPresentation {
    return GoalInfoPresentation(
        goal = goal,
        id = id
    )
}

fun List<GoalInfo>.toGoalInfoPresentationList(): List<GoalInfoPresentation> {
    return this.map { it.toGoalInfoPresentation() }
}

fun GoalsModel.toGoalsPresentation(): GoalsPresentation {
    return GoalsPresentation(
        goals = goals.toGoalInfoPresentationList(),
        completedGoals = completedGoals
    )
}


fun GoalsPresentation.toGoalModel(): GoalsModel {
    return GoalsModel(
        goals = goals.toGoalInfoList(),
        completedGoals = completedGoals
    )
}

fun GoalInfoPresentation.toGoalInfo(): GoalInfo{
    return GoalInfo(
        goal = goal,
        id = id
    )
}


fun List<GoalInfoPresentation>.toGoalInfoList(): List<GoalInfo>{
    return this.map { it.toGoalInfo() }
}


fun PostPresentation.toPostModel(): PostModel{
    return PostModel(
        userName = userName,
        goals = goals.toGoalModel(),
        likeNumber = likeNumber
    )
}