package com.example.dailysync.features.home.presentation.model

import com.example.dailysync.features.home.domain.model.GoalsModel

data class PostPresentation(
    val userName: String,
    val goals: GoalsPresentation,
    val likeNumber: Int
)