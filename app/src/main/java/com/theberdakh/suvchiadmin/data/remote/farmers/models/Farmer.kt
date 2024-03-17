package com.theberdakh.suvchiadmin.data.remote.farmers.models


data class Farmer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phone: String,
    val avatar: String?,
    val username: String,
    val latitude: String,
    val longitude: String,
    val passport: String,
    val gender: String,
    val role: String,
    val regionId: Int,
    val createdAt: String
)
