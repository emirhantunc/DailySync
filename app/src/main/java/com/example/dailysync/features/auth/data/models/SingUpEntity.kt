package com.example.dailysync.features.auth.data.models

data class SingUpEntity(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val followers: List<FollowersAuthEntity> = emptyList()
)