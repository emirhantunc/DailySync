package com.example.dailysync.features.search.data.mapper

import com.example.dailysync.features.search.data.model.SearchEntity
import com.example.dailysync.features.search.domain.model.SearchModel

fun SearchModel.toSearchEntity(): SearchEntity {
    return SearchEntity(
        userName = userName,
        id = id
    )
}


fun SearchEntity.toSearchModel(): SearchModel {
    return SearchModel(
        userName = userName,
        id = id
    )
}

fun List<SearchEntity>.toSearchModelList(): List<SearchModel> {
    return this.map { it.toSearchModel() }
}