package com.theberdakh.suvchiadmin.di

import com.theberdakh.suvchiadmin.data.local.SharedPreferences
import org.koin.dsl.module


val localModule = module {
    single {
        SharedPreferences()
    }
}