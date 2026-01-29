package com.example.dailysync.features.profile.domain.exceptions

sealed class GetProfileError : Throwable() {
    object NetworkError : GetProfileError()
    object UnknownError : GetProfileError()
    object ProfileNotFound : GetProfileError()
}