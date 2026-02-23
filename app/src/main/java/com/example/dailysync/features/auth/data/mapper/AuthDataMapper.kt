package com.example.dailysync.features.auth.data.mapper

import com.example.dailysync.features.auth.data.models.FollowersAuthEntity
import com.example.dailysync.features.auth.data.models.SingInEntity
import com.example.dailysync.features.auth.data.models.SingUpEntity
import com.example.dailysync.features.auth.domain.models.FollowersAuthModel
import com.example.dailysync.features.auth.domain.models.SignInModel
import com.example.dailysync.features.auth.domain.models.SignUpModel

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
        password = password,
        followers = followers.toFollowersAuthEntityList()
    )
}

fun FollowersAuthModel.toFollowersEntity(): FollowersAuthEntity {
    return FollowersAuthEntity(
        id = id,
        name = name
    )
}
fun List<FollowersAuthModel>.toFollowersAuthEntityList():List<FollowersAuthEntity>{
    return this.map { it.toFollowersEntity() }
}

