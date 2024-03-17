package com.theberdakh.suvchiadmin.data.remote.auth.models

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phone: String,
    val avatar: String,
    val username: String,
    val latitude: String,
    val longitude: String,
    val passport: String,
)