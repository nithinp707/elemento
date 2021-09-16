package com.zenora.elemento.common.koincomponents

import com.zenora.elemento.common.network.AppAPIClient.createWebService
import com.zenora.elemento.common.network.AppAPIClient.getUserOkhttpClientWithHeader
import com.zenora.elemento.BuildConfig
import com.zenora.elemento.common.network.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val API_CLIENT_KEY = "webClient"

@Suppress("USELESS_CAST")
var networkModule = module {
    factory(named(API_CLIENT_KEY)) { getUserOkhttpClientWithHeader(get()) }//Setting API client to module
    factory { HeaderInterceptorImpl() as HeaderInterceptor }//setting headers to module
    factory { ConnectivityInterceptorImpl() as ConnectivityInterceptor }//Setting connection check to the repository
    single {
        createWebService<APIInterfaces>("")
    }//Setting the created Retrofit interface to module


}