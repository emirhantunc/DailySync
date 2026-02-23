package com.example.dailysync.features.home.data.mapper

import com.example.dailysync.features.home.data.model.FeedEntity
import com.example.dailysync.features.home.data.model.GoalInfoEntity
import com.example.dailysync.features.home.data.model.GoalsEntity
import com.example.dailysync.features.home.data.model.PostEntity
import com.example.dailysync.features.home.data.model.ThoughtEntity
import com.example.dailysync.features.home.domain.models.FeedModel
import com.example.dailysync.features.home.domain.models.GoalInfo
import com.example.dailysync.features.home.domain.models.GoalsModel
import com.example.dailysync.features.home.domain.models.PostModel
import com.example.dailysync.features.home.domain.models.ThoughtModel


fun GoalsEntity.toGoalModel(): GoalsModel {
    return GoalsModel(
        goals = goals.toGoalInfoList()
    )
}

fun GoalInfoEntity.toGoalInfo(): GoalInfo {
    return GoalInfo(
        goal = goal,
        id = id,
        timeRange = timeRange,
        target = target,
        isCompleted = isCompleted
    )
}

fun List<GoalInfoEntity>.toGoalInfoList(): List<GoalInfo> {
    return this.map { it.toGoalInfo() }
}


fun PostEntity.toPostModel(): PostModel {
    return PostModel(
        postId = postId,
        name = name,
        userId = userId,
        goals = goals.toGoalModel(),
        likeNumber = likeNumber,
        releaseDate = releaseDate
    )
}

fun PostModel.toPostEntity(): PostEntity {
    return PostEntity(
        postId = postId,
        name = name,
        userId = userId,
        goals = goals.toGoalsEntity(),
        likeNumber = likeNumber,
        releaseDate = releaseDate
    )
}

fun GoalsModel.toGoalsEntity(): GoalsEntity {
    return GoalsEntity(
        goals = goals.toGoalInfoEntityList()
    )
}


fun GoalInfo.toGoalInfoEntity(): GoalInfoEntity {
    return GoalInfoEntity(
        goal = goal,
        id = id,
        timeRange = timeRange,
        target = target,
        isCompleted = isCompleted
    )
}

fun List<GoalInfo>.toGoalInfoEntityList(): List<GoalInfoEntity> {
    return this.map { it.toGoalInfoEntity() }
}

fun ThoughtEntity.toThoughtModel(): ThoughtModel {
    return ThoughtModel(
        thoughtId = thoughtId,
        thought = thought,
        name = name,
        userId = userId,
        releaseTime = releaseTime,
        likeNumber = likeNumber
    )
}

fun ThoughtModel.toThoughtEntity(): ThoughtEntity {
    return ThoughtEntity(
        thoughtId = thoughtId,
        thought = thought,
        name = name,
        userId = userId,
        releaseTime = releaseTime,
        likeNumber = likeNumber
    )
}

fun FeedEntity.toFeedModel(): FeedModel {
    return FeedModel(
        id = id,
        feedId = feedId,
        targetId = targetId,
        type = type,
        name = name,
        userId = userId,
        likeNumber = likeNumber,
        releaseTime = releaseTime,
        thoughtContent = thoughtContent,
        goals = goals?.toGoalModel()
    )
}

fun List<FeedEntity>.toFeedModelList(): List<FeedModel> {
    return this.map { it.toFeedModel() }
}


