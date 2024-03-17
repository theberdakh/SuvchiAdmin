package com.theberdakh.suvchiadmin.app

import android.app.Application
import com.theberdakh.suvchiadmin.di.appModule
import com.theberdakh.suvchiadmin.di.localModule
import com.theberdakh.suvchiadmin.di.networkModule
import com.theberdakh.suvchiadmin.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App: Application() {

    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin{
            androidContext(this@App)
            modules(localModule, appModule, networkModule, viewModelModule)
        }
    }


}