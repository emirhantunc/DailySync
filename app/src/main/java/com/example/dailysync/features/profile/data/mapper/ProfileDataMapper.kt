package com.example.dailysync.features.profile.data.mapper

import com.example.dailysync.features.profile.data.model.ProfileEntity
import com.example.dailysync.features.profile.domain.model.ProfileDomain


fun ProfileEntity.toProfileDomain(): ProfileDomain {
    return ProfileDomain(
        name = name,
        id = id
    )
}