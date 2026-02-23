package com.example.dailysync.features.profile.data.models.post

import com.example.dailysync.features.profile.domain.models.post.ProfileGoalsInfoModel

data class ProfileGoalsEntity(
    val goals: List<ProfileGoalsInfoEntity> = emptyList(),
)