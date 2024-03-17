package com.theberdakh.suvchiadmin.ui.contracts

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.contract.ContractsApi
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer

class ContractsPagingSource(private val api: ContractsApi): PagingSource<Int, Contract> (){
    override fun getRefreshKey(state: PagingState<Int, Contract>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Contract> {
        val response = api.getAllContracts()

        val currentPage = params.key ?: 1
        val prevKey = if(currentPage == 1) null else -1
        val nextKey = prevKey?.plus(1)

        return try {
            val regions = mutableListOf<Contract>()
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