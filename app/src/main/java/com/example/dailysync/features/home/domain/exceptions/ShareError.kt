package com.example.dailysync.features.home.domain.exceptions

sealed class ShareError: Throwable(){
    object NetworkError: ShareError()
}