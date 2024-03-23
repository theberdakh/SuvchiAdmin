package com.theberdakh.suvchiadmin.ui.farmer_contracts

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.theberdakh.suvchiadmin.data.remote.contract.ContractsApi
import com.theberdakh.suvchiadmin.data.remote.contract.models.Contract
import com.theberdakh.suvchiadmin.data.remote.contract.models.ContractByFarmerId

class ContractsByFarmerIdPagingSource(private val api: ContractsApi, private val farmerId: Int): PagingSource<Int, ContractByFarmerId>() {
    override fun getRefreshKey(state: PagingState<Int, ContractByFarmerId>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContractByFarmerId> {
        val response = api.getAllContractsByFarmerid(farmerId)

        val currentPage = params.key ?: 1
        val prevKey = if(currentPage == 1) null else -1
        val nextKey = prevKey?.plus(1)

        return try {
            val regions = mutableListOf<ContractByFarmerId>()
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