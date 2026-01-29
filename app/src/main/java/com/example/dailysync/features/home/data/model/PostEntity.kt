package com.example.dailysync.features.home.data.model

import com.example.dailysync.features.home.domain.model.GoalsModel

data class PostEntity(
    val userName: String,
    val goals: GoalsEntity,
    val likeNumber: Int
)