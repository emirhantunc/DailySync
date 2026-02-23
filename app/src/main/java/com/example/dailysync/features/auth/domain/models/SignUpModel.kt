package com.example.dailysync.features.auth.domain.models

data class SignUpModel(
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val followers: List<FollowersAuthModel> = emptyList()
)

