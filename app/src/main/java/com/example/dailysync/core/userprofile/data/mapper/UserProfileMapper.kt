package com.example.dailysync.core.userprofile.data.mapper

import com.example.dailysync.core.userprofile.data.model.CoreProfileEntity
import com.example.dailysync.core.userprofile.domain.model.CoreProfileModel


fun CoreProfileEntity.toProfileModel(): CoreProfileModel {
    return CoreProfileModel(
        name = name,
        id = id,
        followers = followers
    )
}