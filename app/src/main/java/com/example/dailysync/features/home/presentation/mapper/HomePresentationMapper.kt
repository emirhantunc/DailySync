package com.example.dailysync.features.home.presentation.mapper

import com.example.dailysync.features.home.domain.models.FeedModel
import com.example.dailysync.features.home.domain.models.GoalInfo
import com.example.dailysync.features.home.domain.models.GoalsModel
import com.example.dailysync.features.home.domain.models.PostModel
import com.example.dailysync.features.home.domain.models.ThoughtModel
import com.example.dailysync.features.home.presentation.model.FeedPresentation
import com.example.dailysync.features.home.presentation.model.GoalInfoPresentation
import com.example.dailysync.features.home.presentation.model.GoalsPresentation
import com.example.dailysync.features.home.presentation.model.PostPresentation
import com.example.dailysync.features.home.presentation.model.ThoughtPresentation


fun GoalInfo.toGoalInfoPresentation(): GoalInfoPresentation {
    return GoalInfoPresentation(
        goal = goal,
        id = id,
        timeRange = timeRange,
        isCompleted = isCompleted
    )
}

fun List<GoalInfo>.toGoalInfoPresentationList(): List<GoalInfoPresentation> {
    return this.map { it.toGoalInfoPresentation() }
}

fun GoalsModel.toGoalsPresentation(): GoalsPresentation {
    return GoalsPresentation(
        goals = goals.toGoalInfoPresentationList()
    )
}


fun FeedModel.toFeedPresentation(): FeedPresentation {
    return FeedPresentation(
        id = id,
        targetId = targetId,
        type = type,
        name = name,
        userId = userId,
        likeNumber = likeNumber,
        releaseTime = releaseTime,
        thoughtContent = thoughtContent,
        goals = goals?.toGoalsPresentation()
    )
}

fun List<FeedModel>.toFeedPresentationList(): List<FeedPresentation> {
    return this.map { it.toFeedPresentation() }
}




