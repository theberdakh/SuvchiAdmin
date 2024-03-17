package com.theberdakh.suvchiadmin.data.remote.auth.models

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
