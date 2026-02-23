package com.example.dailysync.features.profile.domain.models

data class ProfileModel(
    val name: String = "",
    val id: String = "",
    val followers: List<String> = emptyList()
)