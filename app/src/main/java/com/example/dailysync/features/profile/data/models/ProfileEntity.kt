package com.example.dailysync.features.profile.data.models

import com.google.firebase.firestore.PropertyName

data class ProfileEntity (
    val name: String="",
    @PropertyName("id")
    val id: String="",
    val followers: List<String> = emptyList()
)