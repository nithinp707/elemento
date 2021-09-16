package com.zenora.elemento.common.koincomponents

import com.zenora.elemento.feature.home.viewmodel.HomeViewModel
import com.zenora.elemento.feature.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel() }
}