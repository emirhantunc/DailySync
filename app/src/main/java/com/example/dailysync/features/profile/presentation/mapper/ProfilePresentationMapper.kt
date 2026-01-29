package com.example.dailysync.features.profile.presentation.mapper

import com.example.dailysync.features.profile.domain.model.ProfileDomain
import com.example.dailysync.features.profile.presentation.models.ProfilePresentation


fun ProfileDomain.toProfilePresentation(): ProfilePresentation {
    return ProfilePresentation(
        name = name,
        id = id
    )
}