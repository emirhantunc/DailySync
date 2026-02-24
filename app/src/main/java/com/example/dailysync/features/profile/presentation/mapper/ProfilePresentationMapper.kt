package com.example.dailysync.features.profile.presentation.mapper

import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.features.profile.domain.models.post.ProfileGoalsInfoModel
import com.example.dailysync.features.profile.domain.models.post.ProfileGoalsModel
import com.example.dailysync.features.profile.domain.models.post.ProfilePostModel
import com.example.dailysync.features.profile.presentation.models.post.ProfilePostPresentation
import com.example.dailysync.features.profile.presentation.models.ProfilePresentation
import com.example.dailysync.features.profile.presentation.models.post.ProfileGoalsInfoPresentation
import com.example.dailysync.features.profile.presentation.models.post.ProfileGoalsPresentation


fun CoreProfileModel.toProfilePresentation(): ProfilePresentation {
    return ProfilePresentation(
        name = name,
        id = id,
        followers = followers
    )
}
fun ProfileGoalsModel.toProfileGoalsPresentation(): ProfileGoalsPresentation {
    return ProfileGoalsPresentation(
        goals = goals.toProfileGoalsInfoPresentationList()
    )
}
fun ProfileGoalsInfoModel.toProfileGoalsInfoPresentation(): ProfileGoalsInfoPresentation {
    return ProfileGoalsInfoPresentation(
        goal = goal,
        id = id,
        timeRange = timeRange,
        isCompleted = isCompleted
    )
}

fun List<ProfileGoalsInfoModel>.toProfileGoalsInfoPresentationList():List<ProfileGoalsInfoPresentation>{
    return this.map { it.toProfileGoalsInfoPresentation() }
}

fun ProfilePostModel.toProfilePostPresentation(): ProfilePostPresentation {
    return ProfilePostPresentation(
        postId = postId,
        goals = goals.toProfileGoalsPresentation(),
        name = name,
        userId = userId,
        likeNumber = likeNumber,
        releaseTime = releaseTime
    )
}


fun List<ProfilePostModel>.toProfilePostPresentationList(): List<ProfilePostPresentation> {
    return this.map { it.toProfilePostPresentation() }
}
