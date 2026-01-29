package com.example.dailysync.features.search.presentation.mapper

import com.example.dailysync.features.search.domain.model.SearchModel
import com.example.dailysync.features.search.presentation.model.SearchPresentation


fun SearchModel.toSearchPresentation(): SearchPresentation {
    return SearchPresentation(
        userName = userName,
        id = id
    )
}

fun List<SearchModel>.toSearchPresentationList(): List<SearchPresentation> {
    return this.map { it.toSearchPresentation() }
}