package com.theberdakh.suvchiadmin.di

import com.theberdakh.suvchiadmin.presentation.AdminViewModel
import com.theberdakh.suvchiadmin.presentation.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        AuthViewModel(repository = get())
    }
    viewModel{
        AdminViewModel(repository = get())
    }
}