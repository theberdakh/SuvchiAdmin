package com.theberdakh.suvchiadmin.utils

import android.util.Log
import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import com.theberdakh.suvchiadmin.data.remote.auth.AuthApi
import com.theberdakh.suvchiadmin.di.networkModule
import com.theberdakh.suvchiadmin.presentation.AuthViewModel
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.androidx.viewmodel.ext.android.viewModel

class TokenInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {



        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${SharedPreferences().accessToken}")
            .build()

        val response = chain.proceed(request)
        if (response.code == 401) {
            Log.d("intercept", "refresh token is used")
            response.close()
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${SharedPreferences().refreshToken}")
                .build()
            return chain.proceed(newRequest)
        }

        Log.d("intercept", "access token is used")
        return response
    }
}