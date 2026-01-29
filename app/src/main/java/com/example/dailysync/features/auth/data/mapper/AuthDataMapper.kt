package com.example.dailysync.features.auth.data.mapper

import com.example.dailysync.features.auth.data.model.SingInEntity
import com.example.dailysync.features.auth.data.model.SingUpEntity
import com.example.dailysync.features.auth.domain.model.SignInModel
import com.example.dailysync.features.auth.domain.model.SignUpModel

fun SignInModel.toSingInEntity(): SingInEntity {
    return SingInEntity(
        email = email,
        password = password
    )
}

fun SignUpModel.toSingUpEntity(): SingUpEntity {
    return SingUpEntity(
        email = email,
        name = name,
        password = password
    )
}