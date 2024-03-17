package com.theberdakh.suvchiadmin.data.remote.auth.models

data class CheckTokenResponse(
    val sub: Int,
    val username: String,
    val role: String,
    val iat: Long,
    val exp: Long,
    val aud: String,
    val iss: String
)
