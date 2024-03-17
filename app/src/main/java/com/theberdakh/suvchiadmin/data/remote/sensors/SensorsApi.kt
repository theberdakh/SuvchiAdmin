package com.theberdakh.suvchiadmin.data.remote.sensors

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor
import retrofit2.Response
import retrofit2.http.GET

interface SensorsApi {

    @GET("/admin/sensors")
    suspend fun getAllSensors(): Response<DataResponse<Sensor>>


}