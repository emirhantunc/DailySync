package com.example.dailysync.features.home.data.model

data class ThoughtEntity(
    val thoughtId: String = "",
    val thought: String = "",
    val name: String = "",
    val userId: String = "",
    val releaseTime: Long = 0L,
    val likeNumber: Int = 0
)
