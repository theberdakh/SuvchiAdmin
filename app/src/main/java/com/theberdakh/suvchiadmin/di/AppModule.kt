package com.theberdakh.suvchiadmin.di

import com.theberdakh.suvchiadmin.domain.AdminRepository
import com.theberdakh.suvchiadmin.domain.AuthRepository
import org.koin.dsl.module

val appModule = module {
    single {
        AuthRepository(api = get())
    }
    single {
        AdminRepository(regionsApi = get(), farmersApi = get(), contractsApi = get(), sensorsApi = get(), coordinationApi = get())
    }
}