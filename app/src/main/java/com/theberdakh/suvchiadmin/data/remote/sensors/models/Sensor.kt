package com.theberdakh.suvchiadmin.data.remote.sensors.models

/*    "id": 1,
            "name": "Sensor name",
            "imei": "123456789012345",
            "userId": null,
            "createdAt": "2024-03-11T04:10:50.971Z",
            "updatedAt": "2024-03-11T04:10:50.971Z"*/
data class Sensor(
    val id: Int,
    val name: String,
    val imei: String,
    val userId: Int?,
    val createdAt: String,
    val updatedAt: String

)
