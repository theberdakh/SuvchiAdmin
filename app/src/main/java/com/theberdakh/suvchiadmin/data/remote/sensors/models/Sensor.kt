package com.theberdakh.suvchiadmin.data.remote.sensors.models

data class Sensor(
    val id: Int,
    val name: String,
    val imei: String,
    val userId: Int?,
    val createdAt: String,
    val updatedAt: String

)
