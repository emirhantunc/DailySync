package com.example.dailysync.features.search.presentation.mapper

import com.example.dailysync.features.search.domain.models.SearchModel
import com.example.dailysync.features.search.presentation.models.SearchPresentation


fun SearchModel.toSearchPresentation(): SearchPresentation {
    return SearchPresentation(
        name = name,
        id = id
    )
}

fun List<SearchModel>.toSearchPresentationList(): List<SearchPresentation> {
    return this.map { it.toSearchPresentation() }
}