package com.example.dailysync.features.search.domain.exception

sealed class SearchError: Throwable(){
    object NetworkError: SearchError()
}