package com.theberdakh.suvchiadmin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.theberdakh.suvchiadmin.data.remote.DataResponse
import com.theberdakh.suvchiadmin.data.remote.ResultData
import com.theberdakh.suvchiadmin.data.remote.contract.models.CreateContractRequestBody
import com.theberdakh.suvchiadmin.data.remote.contract.models.CreateContractResponse
import com.theberdakh.suvchiadmin.data.remote.contract.models.UploadFileResponse
import com.theberdakh.suvchiadmin.data.remote.farmers.models.CreateFarmerRequestBody
import com.theberdakh.suvchiadmin.data.remote.farmers.models.Farmer
import com.theberdakh.suvchiadmin.data.remote.regions.models.Region
import com.theberdakh.suvchiadmin.data.remote.sensors.models.CreateSensorRequestBody
import com.theberdakh.suvchiadmin.data.remote.sensors.models.CreateSensorResponse
import com.theberdakh.suvchiadmin.domain.AdminRepository
import com.theberdakh.suvchiadmin.ui.all_coordination.paging.CoordinationPagingSource
import com.theberdakh.suvchiadmin.ui.contracts.ContractsPagingSource
import com.theberdakh.suvchiadmin.ui.all_regions.RegionsPagingSource
import com.theberdakh.suvchiadmin.ui.farmers.FarmersPagingSource
import com.theberdakh.suvchiadmin.ui.sensors.SensorsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody

class AdminViewModel(val repository: AdminRepository) : ViewModel() {

    val responseAllRegionsByStateIdSuccess = MutableSharedFlow<DataResponse<Region>>()
    val responseAllRegionsByStateIdMessage = MutableSharedFlow<String>()
    val responseAllRegionsByStateIdError = MutableSharedFlow<Throwable>()

    val responseCreateFarmerSuccess = MutableSharedFlow<Farmer>()
    val responseCreateFarmerMessage = MutableSharedFlow<String>()
    val responseCreateFarmerError = MutableSharedFlow<Throwable>()

    val responseUploadFileSuccess = MutableSharedFlow<UploadFileResponse>()
    val responseUploadFileMessage = MutableSharedFlow<String>()
    val responseUploadFileError = MutableSharedFlow<Throwable>()

    val responseCreateContractSuccess = MutableSharedFlow<CreateContractResponse>()
    val responseCreateContractMessage = MutableSharedFlow<String>()
    val responseCreateContractError = MutableSharedFlow<Throwable>()

    val responseCreateSensorSuccess = MutableSharedFlow<CreateSensorResponse>()
    val responseCreateSensorMessage = MutableSharedFlow<String>()
    val responseCreateSensorError = MutableSharedFlow<Throwable>()


    val regions = Pager(
        PagingConfig(pageSize = 1)
    ) {
        RegionsPagingSource(repository.regionsApi)
    }.flow.cachedIn(viewModelScope)

    val contracts = Pager(
        PagingConfig(pageSize = 1)
    ) {
        ContractsPagingSource(repository.contractsApi)
    }.flow.cachedIn(viewModelScope)

    val sensors = Pager(
        PagingConfig(pageSize = 1)
    ) {
        SensorsPagingSource(repository.sensorsApi)
    }.flow.cachedIn(viewModelScope)

    val coordination = Pager(
        PagingConfig(pageSize = 1)
    ) {
        CoordinationPagingSource(repository.coordinationApi)
    }.flow.cachedIn(viewModelScope)



    suspend fun createFarmer(
        farmerRequestBody: CreateFarmerRequestBody
    ) {
        repository.createFarmer(farmerRequestBody).onEach {
            when (it) {
                is ResultData.Success -> {
                    responseCreateFarmerSuccess.emit(it.data)
                }
                is ResultData.Message -> {
                    responseCreateFarmerMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    responseCreateFarmerError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun createSensor(name: String, imei: String){
        repository.createSensor(CreateSensorRequestBody(name = name, imei = imei)).onEach {
            when (it) {
                is ResultData.Success -> {
                    responseCreateSensorSuccess.emit(it.data)
                }
                is ResultData.Message -> {
                    responseCreateSensorMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    responseCreateSensorError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


    suspend fun uploadContractFile(
        file: MultipartBody.Part
    ) {
        repository.uploadFile(file).onEach {
            when (it) {
                is ResultData.Success -> {
                    responseUploadFileSuccess.emit(it.data)
                }
                is ResultData.Message -> {
                    responseUploadFileMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    responseUploadFileError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun createContract(title: String, fileId: Int, userId: Int) {
        val userIds = listOf(userId)
        val createContractRequestBody = CreateContractRequestBody(title, fileId, userId = userIds)
        repository.createContract(createContractRequestBody).onEach {
            when(it){
                is ResultData.Success -> responseCreateContractSuccess.emit(it.data)
                is ResultData.Message -> responseCreateContractMessage.emit(it.message)
                is ResultData.Error -> responseCreateContractError.emit(it.error)
            }
        }.launchIn(viewModelScope)
    }



    fun getAllFarmersByRegionId(regionId: Int): Flow<PagingData<Farmer>> {
        val farmersByRegionId = Pager(
            PagingConfig(pageSize = 1)
        ) {
            FarmersPagingSource(repository.farmersApi, regionId)
        }.flow.cachedIn(viewModelScope)

        return farmersByRegionId
    }

    suspend fun getAllRegionsByStateId(stateId: Int) {
        repository.getAllRegionsByStateId(stateId = stateId).onEach {
            when (it) {
                is ResultData.Success -> {
                    responseAllRegionsByStateIdSuccess.emit(it.data)
                }
                is ResultData.Message -> {
                    responseAllRegionsByStateIdMessage.emit(it.message)
                }
                is ResultData.Error -> {
                    responseAllRegionsByStateIdError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


}