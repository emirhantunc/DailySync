package com.example.dailysync.features.auth.domain.exceptions

sealed class SingInError : Throwable() {
    object NetworkError : SingInError()
}