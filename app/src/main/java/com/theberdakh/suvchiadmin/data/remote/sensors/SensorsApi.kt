package com.theberdakh.suvchiadmin.data.remote.sensors

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.sensors.models.AttachSensorRequestBody
import com.theberdakh.suvchiadmin.data.remote.sensors.models.AttachSensorResponse
import com.theberdakh.suvchiadmin.data.remote.sensors.models.CreateSensorRequestBody
import com.theberdakh.suvchiadmin.data.remote.sensors.models.CreateSensorResponse
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SensorsApi {

    @GET("/admin/sensors")
    suspend fun getAllSensors(): Response<DataResponse<Sensor>>

    @POST("/admin/sensors")
    suspend fun createSensor(@Body createSensorRequestBody: CreateSensorRequestBody): Response<CreateSensorResponse>

    @POST("admin/sensors/attach")
    suspend fun attachSensorToFarmer(@Body attachSensorRequestBody: AttachSensorRequestBody): Response<AttachSensorResponse>


}