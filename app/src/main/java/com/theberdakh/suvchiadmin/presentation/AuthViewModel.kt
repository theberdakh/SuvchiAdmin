package com.theberdakh.suvchiadmin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theberdakh.suvchiadmin.data.remote.ResultData
import com.theberdakh.suvchiadmin.data.remote.auth.models.CheckTokenResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginRequest
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.RefreshTokenResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.UserProfile
import com.theberdakh.suvchiadmin.domain.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AuthViewModel(val repository: AuthRepository) : ViewModel() {

    val responseLoginIsSuccessful = MutableSharedFlow<LoginResponse>()
    val responseLoginIsMessage = MutableSharedFlow<String>()
    val responseLoginIsError = MutableSharedFlow<Throwable>()

    val responseCheckTokenIsSuccessful = MutableSharedFlow<CheckTokenResponse>()
    val responseCheckTokenIsMessage = MutableSharedFlow<String>()
    val responseCheckTokenIsError = MutableSharedFlow<Throwable>()

    val responseGetRefreshTokenIsSuccessful = MutableSharedFlow<RefreshTokenResponse>()
    val responseGetRefreshTokenIsMessage = MutableSharedFlow<String>()
    val responseGetRefreshTokenIsError = MutableSharedFlow<Throwable>()

    val responseProfileIsSuccess = MutableSharedFlow<UserProfile>()
    val responseProfileIsMessage = MutableSharedFlow<String>()
    val responseProfileIsError = MutableSharedFlow<Throwable>()



    suspend fun login(username: String, password: String) {
        repository.login(LoginRequest(username, password)).onEach {
            when (it) {
                is ResultData.Success -> {
                    responseLoginIsSuccessful.emit(it.data)
                }

                is ResultData.Message -> {
                    responseLoginIsMessage.emit(it.message)
                }

                is ResultData.Error -> {
                    responseLoginIsError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun checkToken() {
        repository.checkToken().onEach {
            when (it) {
                is ResultData.Success -> {
                    responseCheckTokenIsSuccessful.emit(it.data)
                }

                is ResultData.Message -> {
                    responseCheckTokenIsMessage.emit(it.message)
                }

                is ResultData.Error -> {
                    responseCheckTokenIsError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getRefreshToken(){
        repository.getRefreshToken().onEach {
            when (it) {
                is ResultData.Success -> {
                    responseGetRefreshTokenIsSuccessful.emit(it.data)
                }

                is ResultData.Message -> {
                    responseGetRefreshTokenIsMessage.emit(it.message)
                }

                is ResultData.Error -> {
                    responseGetRefreshTokenIsError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getUserProfile(){
        repository.getUserProfile().onEach {
            when (it) {
                is ResultData.Success -> {
                    responseProfileIsSuccess.emit(it.data)
                }

                is ResultData.Message -> {
                    responseProfileIsMessage.emit(it.message)
                }

                is ResultData.Error -> {
                    responseProfileIsError.emit(it.error)
                }
            }
        }.launchIn(viewModelScope)
    }


}