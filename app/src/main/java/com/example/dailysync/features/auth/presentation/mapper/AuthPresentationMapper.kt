package com.example.dailysync.features.auth.presentation.mapper

import com.example.dailysync.features.auth.domain.models.FollowersAuthModel
import com.example.dailysync.features.auth.domain.models.SignInModel
import com.example.dailysync.features.auth.domain.models.SignUpModel
import com.example.dailysync.features.auth.presentation.models.FollowersAuthPresentation
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
        password = this.password,
        followers = followers.toFollowersAuthModelList()
    )
}


fun FollowersAuthPresentation.toFollowersAuthModel(): FollowersAuthModel{
    return FollowersAuthModel(
        id = this.id,
        name = this.name
    )
}
fun List<FollowersAuthPresentation>.toFollowersAuthModelList():List<FollowersAuthModel>{
    return this.map { it.toFollowersAuthModel() }
}