package com.example.dailysync.features.auth.presentation.mapper

import com.example.dailysync.features.auth.domain.model.SignInModel
import com.example.dailysync.features.auth.domain.model.SignUpModel
import com.example.dailysync.features.auth.presentation.models.SignInPresentation
import com.example.dailysync.features.auth.presentation.models.SignUpPresentation


fun SignInPresentation.toSignModel(): SignInModel {
    return SignInModel(
        email = this.email,
        password = this.password

    )
}


fun SignUpPresentation.toSignModel(): SignUpModel {
    return SignUpModel(
        email = this.email,
        name = this.name,
        password = this.password
    )
}