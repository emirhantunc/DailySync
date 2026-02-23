package com.example.dailysync.features.search.data.models

import com.google.firebase.firestore.PropertyName


data class SearchEntity(
    @get:PropertyName("name")
    val name: String = "",
    @get:PropertyName("id")
    val id: String = ""
)
