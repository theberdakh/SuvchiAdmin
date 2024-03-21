package com.theberdakh.suvchiadmin.data.remote.coordination

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.coordination.models.Coordination
import com.theberdakh.suvchiadmin.data.remote.coordination.models.CreateCoordinationRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CoordinationApi {
    @GET("admin/coordinations?sort[id]=DESC")
    suspend fun getAllCoordination(): Response<DataResponse<Coordination>>

    @POST("/admin/coordinations")
    suspend fun createCoordination(@Body body: CreateCoordinationRequestBody): Response<Coordination>


}