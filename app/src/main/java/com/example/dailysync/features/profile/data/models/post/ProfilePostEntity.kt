package com.example.dailysync.features.profile.data.models.post

import com.example.dailysync.features.home.domain.models.GoalsModel

data class ProfilePostEntity(
    val postId: String = "",
    val name: String = "",
    val userId: String = "",
    val goals: ProfileGoalsEntity = ProfileGoalsEntity(),
    val likeNumber: Int = 0,
    val releaseDate: Long = 0L
)