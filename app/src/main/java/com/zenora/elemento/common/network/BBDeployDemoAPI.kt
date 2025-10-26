package com.zenora.elemento.common.network

import android.os.Looper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class BBDeployDemoAPI {
    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }
        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .okHttpClient(okHttpClient)
            .build()
    }
}