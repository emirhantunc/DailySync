package com.example.dailysync.core.enums

enum class FeedType(val likeFieldName: String, val documentName: String) {
    POST("likedPosts","posts"),
    THOUGHT("likedThoughts","thoughts")
}