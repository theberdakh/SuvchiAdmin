package com.theberdakh.suvchiadmin.ui.all_regions

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.regions.RegionsApi
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region

class RegionsPagingSource(private val api: RegionsApi): PagingSource<Int, Region>() {

    override fun getRefreshKey(state: PagingState<Int, Region>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Region> {

        val response = api.getAllRegionsByStateId(stateId = 1)
        val regions = mutableListOf<Region>()

        return try {
            val currentPage = params.key ?: 1
            val prevKey = if(currentPage == 1) null else -1
            val nextKey = prevKey?.plus(1)
            val data = response.body()!!.data
            regions.addAll(data)

            LoadResult.Page(
                data = regions,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }

    }

}