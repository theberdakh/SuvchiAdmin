package com.theberdakh.suvchiadmin.domain

import com.theberdakh.suvchiadmin.data.remote.ResultData
import com.theberdakh.suvchiadmin.data.remote.auth.AuthApi
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginRequest
import com.theberdakh.suvchiadmin.data.remote.utils.convertToMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(private val api: AuthApi) {

    suspend fun login(loginRequest: LoginRequest) = flow {
        val response = api.login(loginRequest)
        if (response.isSuccessful){
            emit(ResultData.Success(response.body()!!))
        }
        else {
            emit(ResultData.Message(response.code().convertToMessage()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getRefreshToken() = flow {
        val response = api.getRefreshToken()
        if (response.isSuccessful){
            emit(ResultData.Success(response.body()!!))
        }
        else {
            emit(ResultData.Message(response.code().convertToMessage()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun checkToken() = flow {
        val response = api.checkToken()
        if (response.isSuccessful){
            emit(ResultData.Success(response.body()!!))
        }
        else {
            emit(ResultData.Message(response.code().convertToMessage()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)

    suspend fun getUserProfile() = flow {
        val response = api.getUserProfile()
        if (response.isSuccessful){
            emit(ResultData.Success(response.body()!!))
        }
        else {
            emit(ResultData.Message(response.code().convertToMessage()))
        }
    }.catch {
        emit(ResultData.Error(it))
    }.flowOn(Dispatchers.IO)






}