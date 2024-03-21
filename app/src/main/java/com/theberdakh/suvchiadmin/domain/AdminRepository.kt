package com.theberdakh.suvchiadmin.domain

import com.theberdakh.suvchiadmin.data.remote.ResultData
import com.theberdakh.suvchiadmin.data.remote.contract.ContractsApi
import com.theberdakh.suvchiadmin.data.remote.contract.models.CreateContractRequestBody
import com.theberdakh.suvchiadmin.data.remote.coordination.CoordinationApi
import com.theberdakh.suvchiadmin.data.remote.coordination.models.CreateCoordinationRequestBody
import com.theberdakh.suvchiadmin.data.remote.farmers.FarmersApi
import com.theberdakh.suvchiadmin.data.remote.farmers.models.CreateFarmerRequestBody
import com.theberdakh.suvchiadmin.data.remote.regions.RegionsApi
import com.theberdakh.suvchiadmin.data.remote.sensors.SensorsApi
import com.theberdakh.suvchiadmin.data.remote.sensors.models.CreateSensorRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class AdminRepository(val regionsApi: RegionsApi, val farmersApi: FarmersApi, val contractsApi: ContractsApi, val sensorsApi: SensorsApi, val coordinationApi: CoordinationApi) {

    suspend fun createFarmer(createFarmerRequestBody: CreateFarmerRequestBody)= flow {
        val response = farmersApi.createFarmer(farmer = createFarmerRequestBody)
        if (response.isSuccessful) {
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllRegionsByStateId(stateId: Int) = flow {
        val response = regionsApi.getAllRegionsByStateId(stateId = stateId)
        if (response.isSuccessful) {
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllUsersByRegionId(regionId: Int) = flow {
        val response = farmersApi.getAllFarmersByRegionId(regionId)
        if (response.isSuccessful) {
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)



    suspend fun getAllContract() = flow {
        val response = contractsApi.getAllContracts()
        if (response.isSuccessful) {
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun uploadFile(file: MultipartBody.Part) = flow {
        val response = contractsApi.uploadContractFile(file)
        if (response.isSuccessful) {
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.message()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun createContract(contract: CreateContractRequestBody) = flow {
        val response = contractsApi.createContract(contract)
        if (response.isSuccessful){
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.code().toString()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getAllSensors()  = flow {
        val response = sensorsApi.getAllSensors()
        if (response.isSuccessful){
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.code().toString()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun createSensor(createSensorRequestBody: CreateSensorRequestBody) = flow {
        val response = sensorsApi.createSensor(createSensorRequestBody)
        if (response.isSuccessful){
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.code().toString()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun createCoordination(createCoordinationRequestBody: CreateCoordinationRequestBody)= flow {
        val response = coordinationApi.createCoordination(createCoordinationRequestBody)
        if (response.isSuccessful){
            emit(ResultData.Success(checkNotNull(response.body())))
        } else {
            emit(ResultData.Message(response.code().toString()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

}