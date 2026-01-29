package com.example.dailysync.features.home.domain.exceptions

sealed class FetchError: Throwable(){
    object NetworkError: FetchError()
}