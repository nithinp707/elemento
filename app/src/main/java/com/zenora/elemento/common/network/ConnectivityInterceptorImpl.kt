package com.zenora.elemento.common.network

import com.zenora.elemento.BaseApplication
import com.zenora.elemento.R
import com.zenora.elemento.common.isInternetConnected
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Retrofit interceptor class for internet connectivity.
 */

interface ConnectivityInterceptor : Interceptor

class ConnectivityInterceptorImpl : ConnectivityInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isInternetConnected()) {
            throw NoNetworkException()
        } else {
            chain.proceed(chain.request())
        }
    }
}

class NoNetworkException : IOException() {
    override val message: String
        get() = BaseApplication.applicationContext().getString(R.string.no_network)
}