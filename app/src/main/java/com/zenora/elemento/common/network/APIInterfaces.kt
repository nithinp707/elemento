package com.zenora.elemento.common.network

import com.zenora.elemento.feature.login.dataclass.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * interface to manage API call end points and response.
 */

interface APIInterfaces {

    @POST("login/")
    suspend fun userLogin(
        @Body userLoginRequest: LoginRequest
    ): Response<LoginResponse>


    @POST("refresh")
    fun getRefreshToken(): Call<RefreshTokenResponseModel>


    @POST("users/fcmtoken")
    suspend fun updateFcmToken(@Body fcmRequest: FcmRequest): Response<Boolean>
}