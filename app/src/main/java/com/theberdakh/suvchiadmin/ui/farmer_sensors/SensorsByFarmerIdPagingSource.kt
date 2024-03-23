package com.theberdakh.suvchiadmin.ui.farmer_sensors

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.sensors.SensorsApi
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor

class SensorsByFarmerIdPagingSource(val api: SensorsApi, private val farmerId: Int): PagingSource<Int, Sensor>() {
    override fun getRefreshKey(state: PagingState<Int, Sensor>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Sensor> {
        val response = api.getAllSensorsByFarmerId(farmerId)

        val currentPage = params.key ?: 1
        val prevKey = if(currentPage == 1) null else -1
        val nextKey = prevKey?.plus(1)

        return try {
            val regions = mutableListOf<Sensor>()
            val data = response.body()!!.data
            regions.addAll(data)

            LoadResult.Page(
                data = regions,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}