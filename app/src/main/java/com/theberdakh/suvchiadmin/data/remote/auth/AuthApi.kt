package com.theberdakh.suvchiadmin.data.remote.auth

import com.theberdakh.suvchiadmin.data.remote.auth.models.CheckTokenResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginRequest
import com.theberdakh.suvchiadmin.data.remote.auth.models.LoginResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.RefreshTokenResponse
import com.theberdakh.suvchiadmin.data.remote.auth.models.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/sign-in")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    @POST("/auth/refresh-tokens")
    suspend fun getRefreshToken(): Response<RefreshTokenResponse>

    @GET("/auth/me")
    suspend fun checkToken(): Response<CheckTokenResponse>

    @GET("/user/profile")
    suspend fun getUserProfile(): Response<UserProfile>

}