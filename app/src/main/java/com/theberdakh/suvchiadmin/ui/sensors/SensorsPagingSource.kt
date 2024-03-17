package com.theberdakh.suvchiadmin.ui.sensors

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.sensors.SensorsApi
import com.theberdakh.suvchiadmin.data.remote.sensors.models.Sensor

class SensorsPagingSource(val api: SensorsApi): PagingSource<Int, Sensor>() {
    override fun getRefreshKey(state: PagingState<Int, Sensor>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Sensor> {
        val response = api.getAllSensors()

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