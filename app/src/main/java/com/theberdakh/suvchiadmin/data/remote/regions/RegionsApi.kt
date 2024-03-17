package com.theberdakh.suvchiadmin.data.remote.regions

import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RegionsApi {


    @GET("admin/region/state/{stateId}")
    suspend fun getAllRegionsByStateId(@Path("stateId") stateId: Int): Response<DataResponse<Region>>

}