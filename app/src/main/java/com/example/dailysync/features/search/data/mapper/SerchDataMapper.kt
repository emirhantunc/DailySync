package com.example.dailysync.features.search.data.mapper

import com.example.dailysync.features.search.data.models.SearchEntity
import com.example.dailysync.features.search.domain.models.SearchModel


fun SearchEntity.toSearchModel(): SearchModel {
    return SearchModel(
        name = name,
        id = id
    )
}

fun List<SearchEntity>.toSearchModelList(): List<SearchModel> {
    return this.map { it.toSearchModel() }
}