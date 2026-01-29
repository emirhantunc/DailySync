package com.example.dailysync.features.auth.domain.exceptions

sealed class SignUpError : Throwable() {
    object WeakPassword : SignUpError()
    object NetworkError : SignUpError()
    object UnknownError : SignUpError()
}