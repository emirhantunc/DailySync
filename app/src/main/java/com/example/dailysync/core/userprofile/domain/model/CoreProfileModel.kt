package com.example.dailysync.core.userprofile.domain.model

data class CoreProfileModel(
    val name: String = "",
    val id: String = "",
    val followers: List<String> = emptyList()
)