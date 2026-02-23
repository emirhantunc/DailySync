package com.example.dailysync.core.userprofile.data.model


data class CoreProfileEntity(
    val name: String = "",
    val id: String = "",
    val followers: List<String> = emptyList()
)