package com.theberdakh.suvchiadmin.data.remote.sensors.models

data class CreateSensorResponse(
    val name: String,
    val imei: String,
    val userId: Int?,
    val id: Int,
    val createdAt: String,
    val updatedAt: String
)
