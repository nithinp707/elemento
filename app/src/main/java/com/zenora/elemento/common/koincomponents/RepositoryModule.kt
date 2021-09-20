package com.zenora.elemento.common.koincomponents

import com.zenora.elemento.feature.home.repository.HomeRepository
import com.zenora.elemento.feature.home.repository.HomeRepositoryImpl
import com.zenora.elemento.feature.login.repository.LoginRepository
import com.zenora.elemento.feature.login.repository.LoginRepositoryIml
import org.koin.dsl.module

var repositoryModule = module {
    single { LoginRepositoryIml(get()) as LoginRepository }
    single { HomeRepositoryImpl(get()) as HomeRepository }
}