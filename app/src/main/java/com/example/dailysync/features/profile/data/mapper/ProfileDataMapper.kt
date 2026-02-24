package com.example.dailysync.features.profile.data.mapper

import com.example.dailysync.features.profile.data.models.ProfileEntity
import com.example.dailysync.features.profile.data.models.post.ProfileGoalsEntity
import com.example.dailysync.features.profile.data.models.post.ProfileGoalsInfoEntity
import com.example.dailysync.features.profile.data.models.post.ProfilePostEntity
import com.example.dailysync.features.profile.domain.models.ProfileModel
import com.example.dailysync.features.profile.domain.models.post.ProfileGoalsInfoModel
import com.example.dailysync.features.profile.domain.models.post.ProfileGoalsModel
import com.example.dailysync.features.profile.domain.models.post.ProfilePostModel


fun ProfileEntity.toProfileDomain(): ProfileModel {
    return ProfileModel(
        name = name,
        id = id,
        followers = followers
    )
}


fun ProfilePostEntity.toProfilePostDomain(): ProfilePostModel {
    return ProfilePostModel(
        postId = postId,
        name = name,
        userId = userId,
        goals = goals.toProfileGoalsModel(),
        likeNumber = likeNumber,
        releaseTime = releaseTime
    )
}

fun ProfileGoalsEntity.toProfileGoalsModel(): ProfileGoalsModel {
    return ProfileGoalsModel(
        goals = goals.totoProfileGoalsInfoModelList()
    )
}

fun ProfileGoalsInfoEntity.toProfileGoalsInfoModel(): ProfileGoalsInfoModel {
    return ProfileGoalsInfoModel(
        goal = goal,
        id = id,
        timeRange = timeRange,
        isCompleted = isCompleted
    )
}

fun List<ProfileGoalsInfoEntity>.totoProfileGoalsInfoModelList(): List<ProfileGoalsInfoModel> {
    return this.map { it.toProfileGoalsInfoModel() }
}


fun List<ProfilePostEntity>.toProfileDomainList(): List<ProfilePostModel> {
    return this.map { it.toProfilePostDomain() }
}
