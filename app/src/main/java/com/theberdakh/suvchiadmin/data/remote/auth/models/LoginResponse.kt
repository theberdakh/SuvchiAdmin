package com.theberdakh.suvchiadmin.data.remote.auth.models

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val role: String
) {
    companion object {
        const val ROLE_ADMIN = "admin"
    }
}
