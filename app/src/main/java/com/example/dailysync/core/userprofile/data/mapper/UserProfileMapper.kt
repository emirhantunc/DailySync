package com.example.dailysync.core.userprofile.data.mapper

import com.example.dailysync.core.userprofile.data.model.CoreProfileEntity
import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel
import com.example.dailysync.features.profile.data.models.ProfileEntity
import com.example.dailysync.features.profile.domain.models.ProfileModel


fun CoreProfileEntity.toProfileModel(): CoreProfileModel {
    return CoreProfileModel(
        name = name,
        id = id,
        followers = followers
    )
}