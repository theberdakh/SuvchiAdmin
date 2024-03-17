package com.theberdakh.suvchiadmin.ui.farmers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.farmers.FarmersApi
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer

class FarmersPagingSource(private val api: FarmersApi, private val regionId: Int) : PagingSource<Int, Farmer>(){
    override fun getRefreshKey(state: PagingState<Int, Farmer>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Farmer> {
        val response = api.getAllFarmersByRegionId(regionId = regionId)
        val regions = mutableListOf<Farmer>()

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