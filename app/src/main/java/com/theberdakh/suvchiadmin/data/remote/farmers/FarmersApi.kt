package com.theberdakh.suvchiadmin.data.remote.farmers

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.farmers.models.CreateFarmerRequestBody
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FarmersApi {

    @GET("/admin/users/?sort[id]=DESC")
    suspend fun getAllFarmersByRegionId(@Query("regionId") regionId: Int): Response<DataResponse<Farmer>>

    @POST("/admin/users")
    suspend fun createFarmer(@Body farmer: CreateFarmerRequestBody): Response<Farmer>
}