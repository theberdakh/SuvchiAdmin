package com.theberdakh.suvchiadmin.ui.all_coordination.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.coordination.CoordinationApi
import com.theberdakh.suvchiadmin.data.remote.coordination.models.Coordination
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region

/*
* RegionsPagingSource*/
class CoordinationPagingSource(val api: CoordinationApi): PagingSource<Int, Coordination>() {
    override fun getRefreshKey(state: PagingState<Int, Coordination>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Coordination> {
        val response = api.getAllCoordination()
        val allCoordination = mutableListOf<Coordination>()

        return try {
            val currentPage = params.key ?: 1
            val prevKey = if(currentPage == 1) null else -1
            val nextKey = prevKey?.plus(1)
            val data = response.body()!!.data
            allCoordination.addAll(data)

            LoadResult.Page(
                data = allCoordination,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}