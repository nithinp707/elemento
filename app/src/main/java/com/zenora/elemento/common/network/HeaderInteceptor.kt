package com.zenora.elemento.common.network

import com.zenora.elemento.common.SharedPreferenceHelper
import com.zenora.elemento.common.constants.*
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Retrofit interceptor for API request header.
 */

interface HeaderInterceptor : Interceptor

/**
 * Setting up the header to the API client
 */

class HeaderInterceptorImpl : HeaderInterceptor {
    //val CONTENT_TYPE = "Content-Type"
    //val JSON = "application/json"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request()
                .newBuilder()
                .addHeader(
                    AUTHORIZATION,
                    "$BEARER ${SharedPreferenceHelper.getString(PreferenceConstants.ACCESS_TOKEN) ?: ""}"
                )
                .addHeader(CLIENT, APP)
                .addHeader(
                    REFRESH_TOKEN,
                    SharedPreferenceHelper.getString(PreferenceConstants.REFRESH_TOKEN) ?: ""
                )
                .addHeader(LANGUAGE, "1")
                .build()
        return chain.proceed(request)
    }
}