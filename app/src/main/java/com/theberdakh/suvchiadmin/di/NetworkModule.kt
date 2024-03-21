package com.theberdakh.suvchiadmin.di

import com.theberdakh.suvchiadmin.data.remote.auth.AuthApi
import com.theberdakh.suvchiadmin.data.remote.contract.ContractsApi
import com.theberdakh.suvchiadmin.data.remote.coordination.CoordinationApi
import com.theberdakh.suvchiadmin.data.remote.coordination.models.Coordination
import com.theberdakh.suvchiadmin.data.remote.farmers.FarmersApi
import com.theberdakh.suvchiadmin.data.remote.regions.RegionsApi
import com.theberdakh.suvchiadmin.data.remote.sensors.SensorsApi
import com.theberdakh.suvchiadmin.domain.AuthRepository
import com.theberdakh.suvchiadmin.utils.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.sin

val networkModule = module {



    fun provideRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        )

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(TokenInterceptor())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.smartwaterdegree.uz")
            .client(client)
            .build()
    }

    single {
        provideRetrofit()
    }

    single {
        get<Retrofit>().create(AuthApi::class.java)
    }

    single {
        get<Retrofit>().create(RegionsApi::class.java)
    }

    single {
        get<Retrofit>().create(FarmersApi::class.java)
    }
    single {
        get<Retrofit>().create(ContractsApi::class.java)
    }
    single {
        get<Retrofit>().create(SensorsApi::class.java)
    }
    single {
        get<Retrofit>().create(CoordinationApi::class.java)
    }



}